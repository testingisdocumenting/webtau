/*
 * Copyright 2021 webtau maintainers
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

import org.testingisdocumenting.webtau.expectation.ExpectationHandlers;

/**
 * capture test artifacts for usage in documentation
 */
public class CoreDocumentation {
    private static final CoreDocumentationAssertion assertion = findHandlerInRegistered();

    /**
     * last used actual value in <code>should</code> assertion
     */
    public final CoreDocumentationAssertionValue actual = new CoreDocumentationAssertionValue(assertion::actualValue);

    /**
     * last used expected value(s) in <code>should</code> assertion
     */
    public final CoreDocumentationAssertionValue expected = new CoreDocumentationAssertionValue(assertion::expectedValue);

    /**
     * Captures value to a text or JSON file (based on the content) in parent location defined by {@link DocumentationArtifactsLocation}
     *
     * @param artifactName artifact name (file name without extension)
     * @param value value to capture
     */
    public void capture(String artifactName, Object value) {
        DocumentationArtifacts.captureTextOrJson(artifactName, value);
    }

    /**
     * Captures value to a text file in parent location defined by {@link DocumentationArtifactsLocation}
     *
     * @param artifactName artifact name (file name without extension)
     * @param value value to capture
     */
    public void captureText(String artifactName, Object value) {
        DocumentationArtifacts.captureText(artifactName, value);
    }

    /**
     * Captures value to a JSON file in parent location defined by {@link DocumentationArtifactsLocation}
     *
     * @param artifactName artifact name (file name without extension)
     * @param value value to capture
     */
    public void captureJson(String artifactName, Object value) {
        DocumentationArtifacts.captureJson(artifactName, value);
    }

    /**
     * Captures value to a CSV file in parent location defined by {@link DocumentationArtifactsLocation}
     *
     * @param artifactName artifact name (file name without extension)
     * @param value value to capture
     */
    public void captureCsv(String artifactName, Object value) {
        DocumentationArtifacts.captureCsv(artifactName, value);
    }

    private static CoreDocumentationAssertion findHandlerInRegistered() {
        return (CoreDocumentationAssertion) ExpectationHandlers.handlersStream()
                .filter(handler -> handler instanceof CoreDocumentationAssertion)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("CoreDocumentationAssertion must be registered with META-INF/services"));
    }
}
