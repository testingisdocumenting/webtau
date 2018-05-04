package com.twosigma.documentation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentationArtifactsLocation {
    private static AtomicReference<Path> root = new AtomicReference<>(getInitialRoot());

    public static void setRoot(Path newRoot) {
        root.set(newRoot);
    }

    public static Path resolve(String relativePath) {
        return root.get().resolve(relativePath);
    }

    private static Path getInitialRoot() {
        String property = System.getProperty("documentation.artifacts.root");
        return property == null ? Paths.get("doc-artifacts") : Paths.get(property);
    }
}
