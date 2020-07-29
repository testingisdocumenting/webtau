/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http.testserver;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FixedResponsesHandler extends AbstractHandler {
    private final Map<String, TestServerResponse> getResponses = new HashMap<>();
    private final Map<String, TestServerResponse> patchResponses = new HashMap<>();
    private final Map<String, TestServerResponse> postResponses = new HashMap<>();
    private final Map<String, TestServerResponse> putResponses = new HashMap<>();
    private final Map<String, TestServerResponse> deleteResponses = new HashMap<>();

    public void registerGet(String relativeUrl, TestServerResponse response) {
        getResponses.put(relativeUrl, response);
    }

    public void registerPatch(String relativeUrl, TestServerResponse response) {
        patchResponses.put(relativeUrl, response);
    }

    public void registerPost(String relativeUrl, TestServerResponse response) {
        postResponses.put(relativeUrl, response);
    }

    public void registerPut(String relativeUrl, TestServerResponse response) {
        putResponses.put(relativeUrl, response);
    }

    public void registerDelete(String relativeUrl, TestServerResponse response) {
        deleteResponses.put(relativeUrl, response);
    }

    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {

        Map<String, TestServerResponse> responses = findResponses(request);

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement((String) null);
        request.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, multipartConfigElement);

        TestServerResponse testServerResponse = responses.get(baseRequest.getOriginalURI());
        if (testServerResponse == null) {
            response.setStatus(404);
        } else {
            testServerResponse.responseHeader(request).forEach(response::addHeader);

            byte[] responseBody = testServerResponse.responseBody(request);
            response.setStatus(testServerResponse.responseStatusCode());
            response.setContentType(testServerResponse.responseType(request));

            if (responseBody != null) {
                response.getOutputStream().write(responseBody);
            }
        }

        baseRequest.setHandled(true);
    }

    private Map<String, TestServerResponse> findResponses(HttpServletRequest request) {
        switch (request.getMethod()) {
            case "GET":
                return getResponses;
            case "PATCH":
                return patchResponses;
            case "POST":
                return postResponses;
            case "PUT":
                return putResponses;
            case "DELETE":
                return deleteResponses;
            default:
                return Collections.emptyMap();

        }
    }
}
