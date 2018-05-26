/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http;

import com.twosigma.documentation.DocumentationArtifactsLocation;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;

import java.nio.file.Path;

public class HttpDocumentation {
    public void capture(String artifactName) {
        Path path = DocumentationArtifactsLocation.resolve(artifactName);

        HttpValidationResult lastValidationResult = Http.http.getLastValidationResult();
        if (lastValidationResult == null) {
            throw new IllegalStateException("no http calls were made yet");
        }

        String requestHeader = lastValidationResult.getRequestHeader().toString();
        if (! requestHeader.isEmpty()) {
            FileUtils.writeTextContent(path.resolve("request.header.txt"),
                    requestHeader);
        }

        if (lastValidationResult.getRequestType() != null) {
            String fileName = "request." + fileExtensionForType(lastValidationResult.getRequestType());
            FileUtils.writeTextContent(path.resolve(fileName), lastValidationResult.getRequestContent());
        }

        if (lastValidationResult.getResponseType() != null) {
            String fileName = "response." + fileExtensionForType(lastValidationResult.getResponseType());
            FileUtils.writeTextContent(path.resolve(fileName), lastValidationResult.getResponseContent());
        }

        if (lastValidationResult.getPassedPaths() != null) {
            FileUtils.writeTextContent(path.resolve("paths.json"),
                    JsonUtils.serialize(lastValidationResult.getPassedPaths()));
        }
    }

    private String fileExtensionForType(String type) {
        return type.contains("json") ? "json" : "data";
    }
}
