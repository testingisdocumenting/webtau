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

        if (lastValidationResult.getRequestType() != null) {
            String fileName = "request." + fileExtensionForType(lastValidationResult.getRequestType());
            FileUtils.writeTextContent(path.resolve(fileName), lastValidationResult.getRequestContent());
        }

        if (lastValidationResult.getResponseType() != null) {
            String fileName = "response." + fileExtensionForType(lastValidationResult.getResponseType());
            FileUtils.writeTextContent(path.resolve(fileName), lastValidationResult.getResponseContent());
        }
    }

    private String fileExtensionForType(String type) {
        return type.contains("json") ? "json" : "data";
    }
}
