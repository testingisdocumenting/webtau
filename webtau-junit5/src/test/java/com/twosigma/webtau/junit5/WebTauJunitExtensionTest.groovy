package com.twosigma.webtau.junit5


import com.twosigma.webtau.javarunner.report.JavaReport
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
        def report = JavaReport.INSTANCE.create()

        def tests = report.tests.stream().collect(Collectors.toList())
        tests.should == ['*id'                    | 'failed' | 'errored'] {
                         ________________________________________________
                         'beforeAllfailingInit'   | false    | true
                         'afterAllfailingCleanup' | false    | true  }
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
