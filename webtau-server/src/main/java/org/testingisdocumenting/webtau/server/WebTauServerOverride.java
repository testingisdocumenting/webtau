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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Server can register a response based on low-level servlet request.
 */
public interface WebTauServerOverride {
    boolean matchesUri(String method, String uri);

    String overrideId();

    WebTauServerResponse response(HttpServletRequest request);

    default void apply(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        WebTauServerResponse serverResponse = response(servletRequest);
        serverResponse.getHeader().forEach((k, v) -> servletResponse.addHeader(k, v.toString()));

        try {
            byte[] responseBody = serverResponse.getContent();
            servletResponse.setStatus(serverResponse.getStatusCode());
            servletResponse.setContentType(serverResponse.getContentType());

            if (responseBody != null) {
                servletResponse.getOutputStream().write(responseBody);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
