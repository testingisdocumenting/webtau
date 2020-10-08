package org.testingisdocumenting.webtau.cfg;

import org.immutables.value.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation.DEFAULT_DOC_ARTIFACTS_DIR_NAME;

public interface Config {
    String url();

    @Value.Default
    default Path workingDir() {
        return Paths.get("");
    }

    @Value.Default
    default Path docPath() {
        return workingDir().resolve(DEFAULT_DOC_ARTIFACTS_DIR_NAME);
    }
}
