/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server;

import org.apache.commons.io.IOUtils;
import org.testingisdocumenting.webtau.server.route.RouteParams;
import org.testingisdocumenting.webtau.server.route.RouteParamsParser;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebtauServerRequest {
    private final String uri;
    private final String method;
    private final RouteParams routeParams;
    private final String contentType;
    private final String textContent;
    private final byte[] bytesContent;
    private final Map<String, CharSequence> header;
    private final String fullUrl;

    public static WebtauServerRequest create(RouteParamsParser routeParamsParser, HttpServletRequest request) {
        try {
            return new WebtauServerRequest(routeParamsParser.parse(request.getRequestURI()),
                    request.getMethod(),
                    request.getRequestURL().toString(),
                    request.getRequestURI(),
                    request.getContentType(),
                    IOUtils.toByteArray(request.getInputStream()),
                    headerFromRequest(request));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public WebtauServerRequest(RouteParams routeParams,
                               String method,
                               String fullUrl,
                               String uri,
                               String contentType, byte[] bytesContent,
                               Map<String, CharSequence> header) {
        this.routeParams = routeParams;
        this.method = method;
        this.fullUrl = fullUrl;
        this.uri = uri;
        this.contentType = contentType;
        this.bytesContent = bytesContent;
        this.textContent = new String(bytesContent);
        this.header = header;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContentAsBytes() {
        return bytesContent;
    }

    public String getContentAsText() {
        return textContent;
    }

    /**
     * converts json content into map
     *
     * @return json content as map
     */
    public Map<String, ?> getContentAsMap() {
        return JsonUtils.deserializeAsMap(getContentAsText());
    }

    /**
     * converts json content into list
     *
     * @return json content as list
     */
    public List<?> getContentAsList() {
        return JsonUtils.deserializeAsList(getContentAsText());
    }

    public Map<String, CharSequence> getHeader() {
        return header;
    }

    /**
     * get value of route param by name
     *
     * @param routerParamName param name
     * @return router param value
     */
    public String param(String routerParamName) {
        return routeParams.get(routerParamName);
    }

    public RouteParams getRouteParams() {
        return routeParams;
    }

    private static Map<String, CharSequence> headerFromRequest(HttpServletRequest request) {
        Map<String, CharSequence> header = new LinkedHashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            header.put(name, request.getHeader(name));
        }

        return header;
    }
}
