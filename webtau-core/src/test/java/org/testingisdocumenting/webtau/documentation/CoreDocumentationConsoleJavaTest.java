package org.testingisdocumenting.webtau.documentation;

import org.junit.Test;
import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.utils.FileUtils;

import java.nio.file.Path;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class CoreDocumentationConsoleJavaTest {
    @Test
    public void consoleCapture() {
        doc.console.capture("my-output-example", () -> {
            System.out.println("hello no return");
        });

        Path path = WebTauConfig.getCfg().getDocArtifactsPath().resolve("my-output-example.txt");
        actual(FileUtils.fileTextContent(path)).should(equal("hello no return"));
    }

    @Test
    public void consoleCaptureReturnValue() {
        Integer stepResult = doc.console.capture("my-output-return-example", () -> {
            System.out.println("hello return");
            return 42;
        });

        actual(stepResult).should(equal(42));

        Path path = WebTauConfig.getCfg().getDocArtifactsPath().resolve("my-output-return-example.txt");
        actual(FileUtils.fileTextContent(path)).should(equal("hello return"));
    }
}
