/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.junit5;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.testingisdocumenting.webtau.TestListeners;
import org.testingisdocumenting.webtau.javarunner.report.JavaBasedTest;
import org.testingisdocumenting.webtau.javarunner.report.JavaReport;
import org.testingisdocumenting.webtau.report.ReportGenerators;
import org.testingisdocumenting.webtau.reporter.StepReporters;
import org.testingisdocumenting.webtau.reporter.WebTauTest;

public class WebTauJunitPlanListener implements TestExecutionListener {
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        JavaReport.INSTANCE.clear();
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        JavaReport javaReport = JavaReport.INSTANCE;

        JavaBasedTest javaBasedTest = new JavaBasedTest(
                "webtau-after-all-tests",
                "after all tests");

        WebTauTest test = javaBasedTest.getTest();
        test.setShortContainerId("Teardown");
        test.setSynthetic(true);

        javaReport.addTest(test);

        TestListeners.beforeTestRun(test);

        StepReporters.add(javaBasedTest);
        try {
            test.startClock();
            TestListeners.afterAllTests();
            test.stopClock();
            test.setRan(true);
        } catch (Throwable e) {
            test.setExceptionIfNotSet(e);
            throw e;
        } finally {
            StepReporters.remove(javaBasedTest);
            TestListeners.afterTestRun(test);
        }

        javaReport.stopTimer();
        ReportGenerators.generate(javaReport.create());
    }
}
