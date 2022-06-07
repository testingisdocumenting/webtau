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

public class WebTauServerFakeJettyHandler extends AbstractHandler {
    private final String serverId;

    public WebTauServerFakeJettyHandler(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public void handle(String url, Request baseRequest,
                       HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse) throws IOException, ServletException {
        Optional<WebTauServerOverride> optionalOverride = WebTauServerGlobalOverrides.findOverride(serverId,
                servletRequest.getMethod(),
                servletRequest.getRequestURI());

        if (!optionalOverride.isPresent()) {
            servletResponse.setStatus(404);
        } else {
            WebTauServerOverride override = optionalOverride.get();
            override.apply(servletRequest, servletResponse);
        }

        baseRequest.setHandled(true);
    }
}
