package com.twosigma.webtau.http;

import com.atlassian.oai.validator.SwaggerRequestResponseValidator;
import com.atlassian.oai.validator.interaction.ApiOperationResolver;
import com.atlassian.oai.validator.model.ApiOperationMatch;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import java.net.MalformedURLException;
import java.net.URL;

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;
import static java.lang.String.format;

class OpenAPISpecValidator {
    private final SwaggerRequestResponseValidator openApiValidator;
    private final ApiOperationResolver apiOperationResolver;

    OpenAPISpecValidator() {
        this(getCfg().getOpenApiSpecUrl());
    }

    OpenAPISpecValidator(String openApiSpecUrl) {
        if (openApiSpecUrl != null && !openApiSpecUrl.trim().isEmpty()) {
            openApiValidator = SwaggerRequestResponseValidator.createFor(openApiSpecUrl).build();

            final SwaggerDeserializationResult swaggerParseResult =
                    new SwaggerParser().readWithInfo(openApiSpecUrl, null, true);
            final Swagger api = swaggerParseResult.getSwagger();
            if (api == null) {
                throw new IllegalArgumentException(
                        format("Unable to load API descriptor from provided %s:\n\t%s",
                                openApiSpecUrl, swaggerParseResult.getMessages().toString().replace("\n", "\n\t")));
            }
            apiOperationResolver = new ApiOperationResolver(api, null);
        } else {
            openApiValidator = null;
            apiOperationResolver = null;
        }
    }

    void validateApiSpec(String requestMethod, String fullUrl, HttpResponse httpResponse, HttpValidationResult result) {
        if (openApiValidator == null) {
            return;
        }

        String path = extractPath(fullUrl);
        Request.Method method = Enum.valueOf(Request.Method.class, requestMethod);

        ApiOperationMatch apiOperationMatch = apiOperationResolver.findApiOperation(path, method);
        if (!apiOperationMatch.isPathFound()) {
            //TODO add a warning
            return;
        }

        SimpleResponse response = SimpleResponse.Builder
                .status(httpResponse.getStatusCode())
                .withBody(httpResponse.getContent())
                .build();

        ValidationReport validationReport = openApiValidator.validateResponse(path, method, response);
        validationReport.getMessages().forEach(message -> result.addMismatch("API spec validation failure: " + message.toString()));
    }

    private String extractPath(String fullUrl) {
        URL url = null;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException ignore) {
            //We wouldn't get as far as we did if it was an invalid URL so safe to ignore...
        }
        return url.getPath();
    }
}
