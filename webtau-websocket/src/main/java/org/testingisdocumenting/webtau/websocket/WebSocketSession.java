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
import org.testingisdocumenting.webtau.reporter.WebTauStepInputPrettyPrint;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.websocket.WebSocketUtils.*;

public class WebSocketSession {
    private final Session session;
    private final String destination;

    public final WebSocketValue received;

    public WebSocketSession(Session session, String destination, WebSocketMessageListener messageListener) {
        this.session = session;
        this.destination = destination;
        this.received = new WebSocketValue("received", destination, messageListener);
    }

    public String getDestination() {
        return destination;
    }

    public boolean isOpen() {
        return session.isOpen();
    }

    public void close() {
        WebTauStep step = WebTauStep.createStep(tokenizedMessage().action("closing").classifier("websocket").url(destination),
                () -> tokenizedMessage().action("closed").classifier("websocket").url(destination),
                this::closeImpl);

        step.execute(StepReportOptions.REPORT_ALL);
    }

    public void send(String text) {
        sendAsText(text);
    }

    public void send(Map<String, Object> json) {
        sendAsText(json);
    }

    public void send(List<Object> json) {
        sendAsText(json);
    }

    private void sendAsText(Object value) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("sending").classifier("text").action("message").to().url(destination),
                () -> tokenizedMessage().action("sent").classifier("text").action("message").to().url(destination),
                () -> {
                    try {
                        session.getBasicRemote().sendText(convertToText(value));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        step.setInput(new WebTauStepInputPrettyPrint(convertForPrettyPrint(value)));
        step.execute(StepReportOptions.REPORT_ALL);
    }

    private void closeImpl() {
        try {
            session.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String convertToText(Object payload) {
        if (payload instanceof CharSequence) {
            return payload.toString();
        }

        return JsonUtils.serialize(payload);
    }
}
