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

package com.twosigma.webtau.http;

import com.twosigma.documentation.DocumentationArtifactsLocation;
import com.twosigma.webtau.http.validation.HttpValidationResult;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;

import java.nio.file.Path;

public class HttpDocumentation {
    public void capture(String artifactName) {
        Capture capture = new Capture(artifactName);
        capture.capture();
    }

    private static class Capture {
        private final Path path;
        private final HttpValidationResult lastValidationResult;

        public Capture(String artifactName) {
            this.path = DocumentationArtifactsLocation.resolve(artifactName);

            this.lastValidationResult = Http.http.getLastValidationResult();
            if (this.lastValidationResult == null) {
                throw new IllegalStateException("no http calls were made yet");
            }
        }

        public void capture() {
            captureRequestHeader();
            captureRequestBody();
            captureResponseBody();
            capturePaths();
        }

        private void captureRequestHeader() {
            String requestHeader = lastValidationResult.getRequestHeader().toString();
            if (! requestHeader.isEmpty()) {
                FileUtils.writeTextContent(path.resolve("request.header.txt"),
                        requestHeader);
            }
        }

        private void captureRequestBody() {
            if (lastValidationResult.getRequestType() == null
                    || lastValidationResult.isRequestBinary()
                    || lastValidationResult.nullOrEmptyRequestContent()) {
                return;
            }

            String fileName = "request." + fileExtensionForType(lastValidationResult.getRequestType());
            FileUtils.writeTextContent(path.resolve(fileName),
                    prettyPrintContent(lastValidationResult.getRequestType(),
                            lastValidationResult.getRequestContent()));
        }

        private void captureResponseBody() {
            if (lastValidationResult.getResponseType() == null || !lastValidationResult.hasResponseContent()) {
                return;
            }

            String fileName = "response." + fileExtensionForType(lastValidationResult.getResponseType());
            Path fullPath = path.resolve(fileName);

            if (lastValidationResult.getResponse().isBinary()) {
                FileUtils.writeBinaryContent(fullPath, lastValidationResult.getResponse().getBinaryContent());
            } else {
                FileUtils.writeTextContent(fullPath,
                        prettyPrintContent(lastValidationResult.getResponseType(),
                                lastValidationResult.getResponseTextContent()));
            }
        }

        private void capturePaths() {
            if (lastValidationResult.getPassedPaths() == null) {
                return;
            }

            FileUtils.writeTextContent(path.resolve("paths.json"),
                    JsonUtils.serialize(lastValidationResult.getPassedPaths()));
        }
    }

    private static String fileExtensionForType(String type) {
        if (type.contains("/pdf")) {
            return "pdf";
        }

        return isJson(type) ? "json" : "data";
    }

    private static String prettyPrintContent(String type, String content) {
        if (isJson(type)) {
            return JsonUtils.serializePrettyPrint(JsonUtils.deserialize(content));
        }

        return content;
    }

    private static boolean isJson(String type) {
        return type.contains("json");
    }
}
