package org.testingisdocumenting.webtau.featuretesting;

import org.testingisdocumenting.webtau.cfg.Config;
import org.testingisdocumenting.webtau.openapi.OpenApiConfig;
import org.testingisdocumenting.webtau.report.ReportGenerator;
import org.testingisdocumenting.webtau.reporter.WebTauReport;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.nio.file.Paths;

import static org.testingisdocumenting.webtau.featuretesting.AnotherTestConfig.PersonaConfig.withPassword;

public class Example {
    public static void main(String[] args) {
        MyTestConfig cfg = ImmutableMyTestConfig.builder()
                .url("http://localhost")
                .workingDir(Paths.get("/tmp"))
                .ignoreAdditionalProperties(true)
                .reportGenerator(new MyReportGenerator())
                .myCustomProperty("stuff")
                .build();
        System.out.println("====Config which contains open api spec====");
        System.out.println("config: " + cfg);
        System.out.println("report: ");
        cfg.reportGenerator().generate(null);
        exampleUsageOfOptionalCfg(cfg);
        System.out.println();

        AnotherTestConfig cfg2 = ImmutableAnotherTestConfig.builder()
                .url("http://anotherhost")
                .personas(CollectionUtils.aMapOf(
                        "John", withPassword("yo"),
                        "Paul", withPassword("haha")
                ))
                .build();
        System.out.println("====Config which contains personas and no open api spec====");
        System.out.println("config: " + cfg2);
        exampleUsageOfOptionalCfg(cfg2);
        System.out.println();

        System.out.println("====Missing mandatory cfg====");
        ImmutableMyTestConfig.builder().build();
    }

    public static void exampleUsageOfOptionalCfg(Config cfg) {
        OpenApiConfig openApiConfig;
        if (cfg instanceof OpenApiConfig) {
            openApiConfig = (OpenApiConfig) cfg;
        } else {
            openApiConfig = OpenApiConfig.DEFAULT_CFG;
        }

        System.out.println("open api spec: " + openApiConfig.specUrl());
        System.out.println("open api ignore things: " + openApiConfig.ignoreAdditionalProperties());
    }

    static class MyReportGenerator implements ReportGenerator {
        @Override
        public void generate(WebTauReport report) {
            System.out.println("Generating things");
        }
    }
}
