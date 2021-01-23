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

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.doc;

public class CoreDocumentationAssertionValue {
    private final Supplier<Object> valueSupplier;

    CoreDocumentationAssertionValue(Supplier<Object> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    /**
     * Captures value to a text or JSON file (based on the content) in parent location defined by {@link DocumentationArtifactsLocation}
     *
     * @param artifactName artifact name (file name without extension)
     */
    public void capture(String artifactName) {
        doc.capture(artifactName, valueSupplier.get());
    }

    /**
     * Captures value to a text file in parent location defined by {@link DocumentationArtifactsLocation}

     * @param artifactName artifact name (file name without extension)
     */
    public void captureText(String artifactName) {
        doc.captureText(artifactName, valueSupplier.get());
    }

    /**
     * Captures value to a JSON file in parent location defined by {@link DocumentationArtifactsLocation}

     * @param artifactName artifact name (file name without extension)
     */
    public void captureJson(String artifactName) {
        doc.captureJson(artifactName, valueSupplier.get());
    }

    /**
     * Captures value to a CSV file in parent location defined by {@link DocumentationArtifactsLocation}

     * @param artifactName artifact name (file name without extension)
     */
    public void captureCsv(String artifactName) {
        doc.captureCsv(artifactName, valueSupplier.get());
    }
}
