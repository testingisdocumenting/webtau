/*
 * Copyright 2022 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.junit4;

import org.testingisdocumenting.webtau.TestListeners;
import org.testingisdocumenting.webtau.javarunner.report.JavaBasedTest;
import org.testingisdocumenting.webtau.javarunner.report.JavaReport;
import org.testingisdocumenting.webtau.javarunner.report.JavaShutdownHook;
import org.testingisdocumenting.webtau.report.ConsoleReportGenerator;
import org.testingisdocumenting.webtau.report.HtmlReportGenerator;
import org.testingisdocumenting.webtau.report.ReportGenerators;
import org.testingisdocumenting.webtau.reporter.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WebTauRunner extends BlockJUnit4ClassRunner {
    private static final AtomicInteger idGenerator = new AtomicInteger();

    public WebTauRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        List<FrameworkMethod> befores = wrapInWebTauTestEntry(getTestClass()
                .getAnnotatedMethods(BeforeClass.class));
        return befores.isEmpty() ? statement :
                new RunBefores(statement, befores, null);
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters = wrapInWebTauTestEntry(getTestClass()
                .getAnnotatedMethods(AfterClass.class));
        return afters.isEmpty() ? statement :
                new RunAfters(statement, afters, null);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        JavaBasedTest javaBasedTest = createJavaBasedTest(method);
        WebTauTest webTauTest = javaBasedTest.getTest();

        notifier.addListener(new RunListener() {
            @Override
            public void testFailure(Failure failure) {
                webTauTest.setExceptionIfNotSet(failure.getException());
            }
        });

        beforeTestRun(javaBasedTest);
        try {
            super.runChild(method, notifier);
        } catch (Throwable e) {
            webTauTest.setExceptionIfNotSet(e);
            throw e;
        } finally {
            afterTestRun(javaBasedTest);
        }
    }

    private List<FrameworkMethod> wrapInWebTauTestEntry(List<FrameworkMethod> annotatedMethods) {
        return annotatedMethods.stream().map(this::wrapInWebTauTestEntry).collect(Collectors.toList());
    }

    private FrameworkMethod wrapInWebTauTestEntry(FrameworkMethod annotatedMethod) {
        return new WrappedFrameworkMethod(annotatedMethod);
    }

    private void beforeTestRun(JavaBasedTest javaBasedTest) {
        TestListeners.beforeTestRun(javaBasedTest.getTest());
        javaBasedTest.getTest().startClock();
        StepReporters.add(javaBasedTest);
    }

    private void afterTestRun(JavaBasedTest javaBasedTest) {
        WebTauTest webTauTest = javaBasedTest.getTest();
        webTauTest.setRan(true);
        webTauTest.stopClock();
        JavaReport.INSTANCE.addTest(webTauTest);

        StepReporters.remove(javaBasedTest);

        TestResultPayloadExtractors.extract(webTauTest.getSteps().stream())
                .forEach(webTauTest::addTestResultPayload);

        JavaShutdownHook.INSTANCE.noOp();
        TestListeners.afterTestRun(javaBasedTest.getTest());
    }

    private JavaBasedTest createJavaBasedTest(FrameworkMethod method) {
        ReportRegistration.register();

        String canonicalClassName = method.getDeclaringClass().getCanonicalName();

        JavaBasedTest javaBasedTest = new JavaBasedTest(
                method.getName() + idGenerator.incrementAndGet(),
                method.getName());

        WebTauTest webTauTest = javaBasedTest.getTest();
        webTauTest.setClassName(canonicalClassName);
        webTauTest.setShortContainerId(canonicalClassName);

        return javaBasedTest;
    }

    private class WrappedFrameworkMethod extends FrameworkMethod {
        private final FrameworkMethod frameworkMethod;

        WrappedFrameworkMethod(FrameworkMethod frameworkMethod) {
            super(frameworkMethod.getMethod());
            this.frameworkMethod = frameworkMethod;
        }

        @Override
        public Object invokeExplosively(Object target, Object... params) throws Throwable {
            JavaBasedTest javaBasedTest = createJavaBasedTest(frameworkMethod);

            beforeTestRun(javaBasedTest);
            try {
                return super.invokeExplosively(target, params);
            } catch (Throwable e) {
                javaBasedTest.getTest().setException(e);
                throw e;
            } finally {
                afterTestRun(javaBasedTest);
            }
        }
    }

    // add listeners and reporters only once if the WebTau extension was used
    private static class ReportRegistration {
        static {
            actualRegister();
        }

        static void register() {
            // no-op to trigger class load
        }

        private static void actualRegister() {
            TestListeners.add(new ConsoleTestListener());
            StepReporters.add(StepReporters.defaultStepReporter);
            ReportGenerators.add(new HtmlReportGenerator());
            ReportGenerators.add(new ConsoleReportGenerator());
            JavaReport.INSTANCE.startTimer();
        }
    }
}
