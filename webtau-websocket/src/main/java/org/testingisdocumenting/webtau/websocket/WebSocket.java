/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.websocket;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cleanup.DeferredCallsRegistration;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebSocket {
    private static final List<WebSocketSession> createdSessions = new ArrayList<>();
    public static final WebSocket websocket = new WebSocket();

    private WebSocket() {
    }

    public WebSocketSession connect(String destination) {
        WebTauStep step = WebTauStep.createStep(tokenizedMessage().action("connecting").to().classifier("websocket").url(destination),
                (session) -> tokenizedMessage().action("connected").to().classifier("websocket")
                        .url(((WebSocketSession) session).getDestination()),
                () -> {
                    try {
                        return connectImpl(destination);
                    } catch (DeploymentException | IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private WebSocketSession connectImpl(String destination) throws DeploymentException, IOException, URISyntaxException {
        String fullUrl = UrlUtils.isFull(destination) ? destination : buildFullUrl(destination);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketMessageListener messageListener = new WebSocketMessageListener();
        Session session = container.connectToServer(messageListener, new URI(fullUrl));

        WebSocketSession webSocketSession = new WebSocketSession(session, fullUrl, messageListener);
        createdSessions.add(webSocketSession);

        LazyCleanupRegistration.INSTANCE.noOp();

        return webSocketSession;
    }

    private static String buildFullUrl(String relativeUrl) {
        String baseUrl = WebTauConfig.getCfg().getBaseUrl();
        if (baseUrl.isEmpty()) {
            throw new IllegalArgumentException("base url is not set, either use full url or set base url via config");
        }

        return UrlUtils.replaceHttpWithWs(UrlUtils.concat(baseUrl, relativeUrl));
    }

    private static void closeOpenedSessions() {
        createdSessions.stream()
                .filter(WebSocketSession::isOpen)
                .forEach(WebSocketSession::close);
    }

    private static class LazyCleanupRegistration {
        private static final LazyCleanupRegistration INSTANCE = new LazyCleanupRegistration();

        private LazyCleanupRegistration() {
            DeferredCallsRegistration.callAfterAllTests("closing", "closed", "websocket active sessions",
                    () -> createdSessions.stream().anyMatch(WebSocketSession::isOpen),
                    WebSocket::closeOpenedSessions);
        }

        private void noOp() {
        }
    }
}
