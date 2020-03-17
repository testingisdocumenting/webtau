/*
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

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentationArtifactsLocation {
    private static AtomicReference<Path> root = new AtomicReference<>(getInitialRoot());

    public static void setRoot(Path newRoot) {
        root.set(newRoot);
    }

    public static Path getRoot() {
        return root.get();
    }

    public static Path resolve(String artifactName) {
        return root.get().resolve(artifactName);
    }

    public static Path classBasedLocation(Class<?> testClass) {
        try {
            return Paths.get(testClass.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getInitialRoot() {
        String property = System.getProperty("documentation.artifacts.root");
        return property == null ? Paths.get("doc-artifacts") : Paths.get(property);
    }
}
