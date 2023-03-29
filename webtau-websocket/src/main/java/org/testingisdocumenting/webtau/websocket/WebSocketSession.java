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
import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import javax.websocket.Session;
import java.io.IOException;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebSocketSession {
    private final Session session;
    private final String destination;

    public final WebSocketValue received;

    public WebSocketSession(Session session, String destination, WebSocketMessageListener messageListener) {
        this.session = session;
        this.destination = destination;
        this.received = new WebSocketValue("received", destination, messageListener);
    }

    public Session getSession() {
        return session;
    }

    public void sendText(String text) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("sending").classifier("text").action("message").to().url(destination),
                () -> tokenizedMessage().action("sent").classifier("text").action("message").to().url(destination),
                () -> {
                    try {
                        session.getBasicRemote().sendText(text);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        step.setInput(new WebTauStepInputPrettyPrint(convertForPrettyPrint(text)));
        step.execute(StepReportOptions.REPORT_ALL);
    }

    private static Object convertForPrettyPrint(String original) {
        if (JsonUtils.looksLikeJson(original)) {
            try {
                return JsonUtils.deserialize(original);
            } catch (JsonParseException e) {
                return original;
            }
        }

        return original;
    }
}
