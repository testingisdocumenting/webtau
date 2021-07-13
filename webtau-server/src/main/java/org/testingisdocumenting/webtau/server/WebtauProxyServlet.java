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

import org.eclipse.jetty.proxy.ProxyServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class WebtauProxyServlet extends ProxyServlet {
    private String urlToProxy;
    private String serverId;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        urlToProxy = config.getInitParameter("urlToProxy");
        serverId = config.getInitParameter("serverId");
    }

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest) {
        return urlToProxy + clientRequest.getRequestURI();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<WebtauServerOverride> override = WebtauServerGlobalOverrides.findOverride(serverId,
                request.getMethod(),
                request.getRequestURI());

        if (override.isPresent()) {
            // TODO full response set including headers
            // right now it is just for timeout
            override.get().responseBody(request);
        } else {
            super.service(request, response);
        }
    }
}
