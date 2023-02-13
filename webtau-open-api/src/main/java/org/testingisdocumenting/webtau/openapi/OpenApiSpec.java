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

package org.testingisdocumenting.webtau.openapi;

import com.atlassian.oai.validator.interaction.ApiOperationResolver;
import com.atlassian.oai.validator.model.ApiOperationMatch;
import com.atlassian.oai.validator.model.Request;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.lang.String.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;

public class OpenApiSpec {
    private final OpenAPI api;
    private final ApiOperationResolver apiOperationResolver;
    private final OpenApiSpecLocation specLocation;
    private final String specContent;
    private final boolean isSpecDefined;
    private final Map<OpenApiOperation, Set<String>> operationsAndResponses;

    public OpenApiSpec(OpenApiSpecLocation specLocation) {
        this.specLocation = specLocation;

        isSpecDefined = specLocation.isDefined();
        specContent = isSpecDefined ? readSpecContent() : "";

        api = createOpenAPI();
        apiOperationResolver = api != null ? new ApiOperationResolver(api, null) : null;

        operationsAndResponses = isSpecDefined ? enumerateOperations() : Collections.emptyMap();
    }

    public boolean isSpecDefined() {
        return isSpecDefined;
    }

    public String getSpecContent() {
        return specContent;
    }

    public Stream<OpenApiOperation> availableOperationsStream() {
        return operationsAndResponses.keySet().stream();
    }

    public Optional<OpenApiOperation> findApiOperation(String method, String path) {
        String relativePath = UrlUtils.extractPath(path);

        Request.Method requestMethod = Enum.valueOf(Request.Method.class, method);

        ApiOperationMatch apiOperation = apiOperationResolver.findApiOperation(relativePath, requestMethod);
        if (!apiOperation.isPathFound() || !apiOperation.isOperationAllowed()) {
            return Optional.empty();
        }

        return Optional.of(new OpenApiOperation(method, apiOperation.getApiOperation().getApiPath().original()));
    }

    public Set<String> getDeclaredResponses(OpenApiOperation op) {
        return operationsAndResponses.getOrDefault(op, Collections.emptySet());
    }

    private Map<OpenApiOperation, Set<String>> enumerateOperations() {
        Map<OpenApiOperation, Set<String>> operationsAndResponses = new LinkedHashMap<>();

        api.getPaths().forEach((url, path) -> {

            if (path.getGet() != null) {
                addOperation(operationsAndResponses, "GET", url, path.getGet());
            }

            if (path.getPut() != null) {
                addOperation(operationsAndResponses, "PUT", url, path.getPut());
            }

            if (path.getPost() != null) {
                addOperation(operationsAndResponses, "POST", url, path.getPost());
            }

            if (path.getDelete() != null) {
                addOperation(operationsAndResponses, "DELETE", url, path.getDelete());
            }
        });

        return operationsAndResponses;
    }

    private static void addOperation(Map<OpenApiOperation, Set<String>> operationsAndResponses, String method, String fullUrl, Operation operation) {
        Set<String> responseCodes = operation.getResponses() == null ? Collections.emptySet() : operation.getResponses().keySet();
        operationsAndResponses.put(new OpenApiOperation(method, fullUrl), responseCodes);
    }

    private OpenAPI createOpenAPI() {
        if (!isSpecDefined) {
            return null;
        }

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);

        SwaggerParseResult swaggerParseResult = new OpenAPIParser().readContents(
                specContent, Collections.emptyList(), parseOptions);

        OpenAPI openAPI = swaggerParseResult.getOpenAPI();

        if (openAPI == null) {
            throw new IllegalArgumentException(
                    format("Unable to load API descriptor from provided %s: %s", specLocation.getAsString(),
                            parseResultMessage(swaggerParseResult)));
        }

        return openAPI;
    }

    private static String parseResultMessage(SwaggerParseResult swaggerParseResult) {
        List<String> messages = swaggerParseResult.getMessages();
        if (messages == null) {
            return "check if it is accessible";
        }

        return "\n    " + String.join("\n    ", messages);
    }

    private String readSpecContent() {
        TokenizedMessage openApiTokenClassifier = tokenizedMessage().classifier("open API spec");
        TokenizedMessage typeTokenClassifier = tokenizedMessage().classifier(specLocation.isFileSystem() ? "file system" : "http url");
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("reading").add(openApiTokenClassifier)
                        .from().add(typeTokenClassifier).url(specLocation.getOriginalValue()),
                        () -> tokenizedMessage().action("read").add(openApiTokenClassifier)
                                .from().add(typeTokenClassifier).url(specLocation.getAsString()),
                        () -> specLocation.isFileSystem() ?
                                readSpecContentFromFs() :
                                readSpecContentFromUrl()
                        );

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private String readSpecContentFromUrl() {
        // we use http.get method here to get all the report tracing
        AtomicReference<Object> specBody = new AtomicReference<>();
        OpenApi.withoutValidation(() -> specBody.set(http.get(specLocation.getUrl(), (header, body) -> body)));

        return JsonUtils.serialize(specBody.get());
    }

    private String readSpecContentFromFs() {
        Path path = specLocation.getPath();
        return FileUtils.fileTextContent(path);
    }
}
