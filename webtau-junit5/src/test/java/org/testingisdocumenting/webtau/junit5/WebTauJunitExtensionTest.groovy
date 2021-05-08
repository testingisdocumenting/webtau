package org.testingisdocumenting.webtau.junit5


import org.testingisdocumenting.webtau.javarunner.report.JavaReport
import org.testingisdocumenting.webtau.TestListener
import org.testingisdocumenting.webtau.TestListeners
import org.testingisdocumenting.webtau.reporter.WebTauTest
import org.junit.jupiter.api.Test
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.Launcher
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory

import java.util.stream.Collectors

class WebTauJunitExtensionTest {
    @Test
    void "failed static init methods should have exception attached"() {
        runTest(WithFailedStaticInitAndClean)

        reportTests.should == ['*id'                    | 'failed' | 'errored'] {
                               ________________________________________________
                               'beforeAllfailingInit'   | false    | true
                               'afterAllfailingCleanup' | false    | true  }
    }

    @Test
    void "afterLastTestStatement thrown exception should be part of a test"() {
        def listener = new TestListener() {
            @Override
            void beforeFirstTestStatement(WebTauTest test) {
                if (test.scenario == 'noOpTwo') {
                    throw new RuntimeException("pre run validation")
                }
            }

            @Override
            void afterLastTestStatement(WebTauTest test) {
                if (test.scenario == 'noOpOne') {
                    throw new RuntimeException("post run validation")
                }
            }
        }

        TestListeners.add(listener)
        try {
            runTest(WithFailedPostStepTest)
        } finally {
            TestListeners.remove(listener)
        }

        reportTests.should == ['*scenario' | 'failed' | 'errored' | 'exception'] {
                               ____________________________________________________________________
                               'noOpOne'   | false    | true      | [message: 'post run validation']
                               'noOpTwo'   | false    | true      | [message: 'pre run validation']}
    }

    private static List<WebTauTest> getReportTests() {
        def report = JavaReport.INSTANCE.create()
        return report.tests.stream().collect(Collectors.toList())
    }

    private static void runTest(Class testClass) {
        JavaReport.INSTANCE.clear()

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(testClass))
                .build()
        Launcher launcher = LauncherFactory.create()
        launcher.execute(request)
    }
}
