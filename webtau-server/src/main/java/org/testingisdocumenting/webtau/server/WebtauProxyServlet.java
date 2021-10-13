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

import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;
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
import java.util.Optional;

public class WebtauProxyServlet extends ProxyServlet {
    private final WebtauServerJournal journal;
    private final String urlToProxy;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public WebtauProxyServlet(WebtauServerJournal journal, String urlToProxy) {
        this.journal = journal;
        this.urlToProxy = urlToProxy;
    }

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest) {
        return urlToProxy + clientRequest.getRequestURI();
    }

    @Override
    protected ContentProvider proxyRequestContent(HttpServletRequest request, HttpServletResponse response, Request proxyRequest) throws IOException {
        ContentProvider contentProvider = super.proxyRequestContent(request, response, proxyRequest);
        ContentCaptureRequestWrapper captureRequestWrapper = (ContentCaptureRequestWrapper) request;
        System.out.println("### " + captureRequestWrapper.getCaptureAsString());

        return contentProvider;
    }

    @Override
    protected void onResponseContent(HttpServletRequest request, HttpServletResponse response, Response proxyResponse, byte[] buffer, int offset, int length, Callback callback) {
        super.onResponseContent(request, response, proxyResponse, buffer, offset, length, callback);

        System.out.println("@@@ response wrapper: " + ((ContentCaptureResponseWrapper)response).getCaptureAsString());
    }

    @Override
    protected void onProxyResponseSuccess(HttpServletRequest clientRequest, HttpServletResponse proxyResponse, Response serverResponse) {
        long endTime = Time.currentTimeMillis();
        WebtauServerHandledRequest handledRequest = new WebtauServerHandledRequest(clientRequest, proxyResponse,
                0, endTime,
                (ContentCaptureRequestWrapper) clientRequest,
                (ContentCaptureResponseWrapper) proxyResponse);
        journal.registerCall(handledRequest);

        super.onProxyResponseSuccess(clientRequest, proxyResponse, serverResponse);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<WebtauServerOverride> override = WebtauServerGlobalOverrides.findOverride(journal.getServerId(),
                request.getMethod(),
                request.getRequestURI());

        ContentCaptureRequestWrapper requestWrapper = new ContentCaptureRequestWrapper(request);
        ContentCaptureResponseWrapper responseWrapper = new ContentCaptureResponseWrapper(response);

        startTime.set(Time.currentTimeMillis());
        if (override.isPresent()) {
            override.get().apply(requestWrapper, responseWrapper);
        } else {
            super.service(requestWrapper, responseWrapper);
        }
    }
}
