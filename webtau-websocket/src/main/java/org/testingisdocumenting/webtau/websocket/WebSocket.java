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

import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebSocket {
    public static final WebSocket websocket = new WebSocket();

    private WebSocket() {
    }

    public WebSocketSession connect(String destination) {
        WebTauStep step = WebTauStep.createStep(tokenizedMessage().action("connecting").to().classifier("websocket").url(destination),
                () -> tokenizedMessage().action("connected").to().classifier("websocket").url(destination),
                () -> {
                    try {
                        return connectImpl(destination);
                    } catch (DeploymentException | IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private static WebSocketSession connectImpl(String destination) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketMessageListener messageListener = new WebSocketMessageListener();
        Session session = container.connectToServer(messageListener, new URI(destination));

        return new WebSocketSession(session, destination, messageListener);
    }
}
