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

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class WebtauServerFakeJettyHandler extends AbstractHandler {
    private final String serverId;

    public WebtauServerFakeJettyHandler(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest servletRequest, HttpServletResponse response) throws IOException, ServletException {
        Optional<WebtauServerOverride> optionalOverride = WebtauServerGlobalOverrides.findOverride(serverId,
                servletRequest.getMethod(),
                servletRequest.getRequestURI());

        if (!optionalOverride.isPresent()) {
            response.setStatus(404);
        } else {
            WebtauServerOverride override = optionalOverride.get();
            override.responseHeader(servletRequest).forEach(response::addHeader);

            byte[] responseBody = override.responseBody(servletRequest);
            response.setStatus(override.responseStatusCode(servletRequest));
            response.setContentType(override.responseType(servletRequest));

            if (responseBody != null) {
                response.getOutputStream().write(responseBody);
            }
        }

        baseRequest.setHandled(true);
    }
}
