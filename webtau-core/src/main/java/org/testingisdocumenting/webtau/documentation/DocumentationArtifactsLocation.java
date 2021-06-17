/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.documentation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Captured test artifacts core location.
 * By default the location is "doc-artifacts" dir in the working dir.
 *
 * Can be changed using system property <code>documentation.artifacts.root</code>, via
 * <code>setRoot</code> method, or via config file when webtau-config module is present (using any of modules like http/browser/etc)
 */
public class DocumentationArtifactsLocation {
    public static final String DEFAULT_DOC_ARTIFACTS_DIR_NAME = "doc-artifacts";

    private static final AtomicReference<Path> root = new AtomicReference<>(getInitialRoot());

    public static void setRoot(Path newRoot) {
        root.set(newRoot);
    }

    public static Path getRoot() {
        return root.get();
    }

    public static Path resolve(String artifactName) {
        return root.get().resolve(artifactName);
    }

    private static Path getInitialRoot() {
        String property = System.getProperty("documentation.artifacts.root");
        return property == null ? Paths.get(DEFAULT_DOC_ARTIFACTS_DIR_NAME) : Paths.get(property);
    }
}
