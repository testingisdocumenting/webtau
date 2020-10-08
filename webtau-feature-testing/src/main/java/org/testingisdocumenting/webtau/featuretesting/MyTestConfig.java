package org.testingisdocumenting.webtau.featuretesting;

import org.immutables.value.Value.Immutable;
import org.testingisdocumenting.webtau.cfg.Config;
import org.testingisdocumenting.webtau.openapi.OpenApiConfig;
import org.testingisdocumenting.webtau.report.ReportGenerator;

@Immutable
public interface MyTestConfig extends Config, OpenApiConfig {
    ReportGenerator reportGenerator();

    String myCustomProperty();
}
