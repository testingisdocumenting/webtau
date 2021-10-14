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
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;
import org.eclipse.jetty.util.Callback;
import org.testingisdocumenting.webtau.server.registry.ContentCaptureRequestWrapper;
import org.testingisdocumenting.webtau.server.registry.ContentCaptureResponseWrapper;
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
    protected Response.CompleteListener newProxyResponseListener(HttpServletRequest clientRequest, HttpServletResponse proxyResponse) {
        ProxyResponseListener original = (ProxyResponseListener) super.newProxyResponseListener(clientRequest, proxyResponse);
        List<ByteBuffer> outputCopies = new ArrayList<>();

        return new ProxyResponseListener(clientRequest, proxyResponse) {
            @Override
            public void onBegin(Response serverResponse) {
                original.onBegin(serverResponse);
            }

            @Override
            public void onHeaders(Response serverResponse) {
                original.onHeaders(serverResponse);
            }

            @Override
            public void onContent(Response serverResponse, ByteBuffer content, Callback callback) {
                ByteBuffer copy = ByteBuffer.allocate(content.remaining());
                copy.put(content).flip();
                content.rewind();
                outputCopies.add(copy);

                original.onContent(serverResponse, content, callback);
            }

            @Override
            public void onSuccess(Response serverResponse) {
                original.onSuccess(serverResponse);
            }

            @Override
            public void onComplete(Result result) {
                WebtauServerHandledRequest handledRequest = new WebtauServerHandledRequest(clientRequest, proxyResponse,
                        (Long) clientRequest.getAttribute(START_TIME_ATTR_KEY),
                        Time.currentTimeMillis(),
                        ((ContentCaptureRequestWrapper) clientRequest).getCaptureAsString(),
                        extractTextFromOutputs(outputCopies));
                journal.registerCall(handledRequest);

                original.onComplete(result);
            }

            @Override
            public void succeeded() {
                original.succeeded();
            }

            @Override
            public void failed(Throwable failure) {
                original.failed(failure);
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
        ContentCaptureResponseWrapper responseWrapper = new ContentCaptureResponseWrapper(response);
        requestWrapper.setAttribute(START_TIME_ATTR_KEY, Time.currentTimeMillis());

        if (override.isPresent()) {
            override.get().apply(requestWrapper, responseWrapper);
        } else {
            super.service(requestWrapper, responseWrapper);
        }
    }
}
