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

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;
import org.testingisdocumenting.webtau.server.registry.ContentCaptureRequestWrapper;
import org.testingisdocumenting.webtau.server.registry.WebtauServerHandledRequest;
import org.testingisdocumenting.webtau.server.registry.WebtauServerJournal;
import org.testingisdocumenting.webtau.time.Time;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebtauProxyServlet extends AsyncMiddleManServlet {
    private static final String START_TIME_ATTR_KEY = "org.testingisdocumenting.webtau.server.startTime";

    private final WebtauServerJournal journal;
    private final String urlToProxy;

    public WebtauProxyServlet(WebtauServerJournal journal, String urlToProxy) {
        this.journal = journal;
        this.urlToProxy = urlToProxy;
    }

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest) {
        return urlToProxy + clientRequest.getRequestURI();
    }

    @Override
    protected ProxyWriter newProxyWriteListener(HttpServletRequest clientRequest, Response proxyResponse) {
        return super.newProxyWriteListener(clientRequest, proxyResponse);
    }

    @Override
    protected ContentTransformer newServerResponseContentTransformer(HttpServletRequest clientRequest, HttpServletResponse proxyResponse, Response serverResponse) {
        List<ByteBuffer> copies = new ArrayList<>();

        return (input, finished, output) -> {
            ByteBuffer copy = ByteBuffer.allocate(input.remaining());
            copy.put(input).flip();
            input.rewind();
            copies.add(copy);

            output.add(input);

            if (finished) {
                WebtauServerHandledRequest handledRequest = new WebtauServerHandledRequest(clientRequest, proxyResponse,
                        (Long) clientRequest.getAttribute(START_TIME_ATTR_KEY),
                        Time.currentTimeMillis(),
                        ((ContentCaptureRequestWrapper) clientRequest).getCaptureAsString(),
                        extractTextFromOutputs(copies));
                journal.registerCall(handledRequest);
            }
        };
    }

    private String extractTextFromOutputs(List<ByteBuffer> output) {
        StringBuilder result = new StringBuilder();
        for (ByteBuffer buffer : output) {
            result.append(StandardCharsets.UTF_8.decode(buffer));
        }

        return result.toString();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<WebtauServerOverride> override = WebtauServerGlobalOverrides.findOverride(journal.getServerId(),
                request.getMethod(),
                request.getRequestURI());

        ContentCaptureRequestWrapper requestWrapper = new ContentCaptureRequestWrapper(request);
        request.setAttribute(START_TIME_ATTR_KEY, Time.currentTimeMillis());

        if (override.isPresent()) {
            override.get().apply(requestWrapper, response);
        } else {
            super.service(requestWrapper, response);
        }
    }
}
