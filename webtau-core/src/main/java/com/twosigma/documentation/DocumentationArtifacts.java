package com.twosigma.documentation;

import com.twosigma.webtau.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentationArtifacts {
    public static void create(Class testClass, String artifactName, String textContent) {
        Path path = Paths.get(testClass.getProtectionDomain().getCodeSource().getLocation().getPath())
                .resolve(artifactName);
        FileUtils.writeTextContent(path, textContent);
    }
}
