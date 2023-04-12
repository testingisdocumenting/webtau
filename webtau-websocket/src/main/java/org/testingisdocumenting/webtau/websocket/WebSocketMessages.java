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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.datanode.DataNodeId;
import org.testingisdocumenting.webtau.data.datanode.ValueExtractorByPath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebSocketMessages implements ActualValueExpectations, ActualValueAware, ActualPathAndDescriptionAware {
    private final String label;
    private final ValuePath valuePath;
    private final String destination;
    private final WebSocketMessageListener messageListener;
    private Object lastConvertedMessage;

    private final DataNodeId nodeToExtract;

    public final WebSocketMessagesCount count;

    public WebSocketMessages(String label, String destination, DataNodeId nodeToExtract, WebSocketMessageListener messageListener) {
        this.label = label;
        this.valuePath = new ValuePath(label);
        this.destination = destination;
        this.nodeToExtract = nodeToExtract;
        this.messageListener = messageListener;
        this.count = new WebSocketMessagesCount(this, messageListener);
    }

    public WebSocketMessages get(String path) {
        return new WebSocketMessages(label, destination, nodeToExtract.concat(path), messageListener);
    }

    /**
     * WebTau receives and stores asynchronous messages behind the scene.
     * <p>
     * Use <code>pollAsText</code> to access messages in a synchronous manner.
     * If message is not yet received, <code>pollAsText</code> will wait for a configured (<code>webSocketPollTimeout</code>) milliseconds (default 5 seconds).
     * @return polled message
     */
    public String pollAsText() {
        return pollAsText(WebSocketConfig.getWebSocketPollTimeout());
    }

    public String pollAsText(long timeOutMillis) {
        return (String) runPollMessageStep(null, timeOutMillis, WebSocketUtils::convertToJsonIfPossible).original;
    }

    public <R> R poll() {
        return poll(WebSocketConfig.getWebSocketPollTimeout());
    }

    @SuppressWarnings("unchecked")
    public <R> R poll(long timeOutMillis) {
        return (R) runPollMessageStep(null, timeOutMillis, WebSocketUtils::convertToJsonIfPossible).converted;
    }

    /**
     * discards all received messages
     */
    public void discard() {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("discarding all").classifier("messages").add(describe()),
                () -> tokenizedMessage().action("discarded all").classifier("messages").add(describe()),
                () -> messageListener.getMessages().clear());

        step.execute(StepReportOptions.SKIP_START);
    }

    @Override
    public Object actualValue() {
        return actualValue(0, 0, 0);
    }

    @Override
    public Object actualValue(int attemptIdx, long tickMillis, long timeOutMillis) {
        if (attemptIdx == 0) {
            lastConvertedMessage = null;
        }

        PolledMessage message = runPollMessageStep(lastConvertedMessage, tickMillis, WebSocketUtils::convertToJsonIfPossible);
        lastConvertedMessage = message.converted;

        return message.convertedAndExtracted;
    }

    @Override
    public ValuePath actualPath() {
        return valuePath;
    }

    @Override
    public TokenizedMessage describe() {
        DataNodeId id = new DataNodeId(label).concat(nodeToExtract.getPath());
        return tokenizedMessage().id(id.getPath()).from().url(destination);
    }

    @Override
    public ExpectationTimer createExpectationTimer() {
        return new WebSocketValueExpectationTimer(messageListener);
    }

    private PolledMessage runPollMessageStep(Object lastConvertedMessage, long tickMillis, Function<String, Object> converter) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("polling").classifier("websocket message"),
                (result) -> ((PolledMessage) result).isNewMessage ?
                        tokenizedMessage().action("polled new message") :
                        tokenizedMessage().action("no new message is polled"),
                () -> {
                    try {
                        return new PolledMessage(messageListener.getMessages().poll(tickMillis, TimeUnit.MILLISECONDS), lastConvertedMessage, converter, nodeToExtract);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        step.setStepOutputFunc((result) -> ((PolledMessage)result).isNewMessage ?
                new WebTauStepOutputPrettyPrint(((PolledMessage) result).converted) :
                WebTauStepOutput.EMPTY);
        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private static class PolledMessage {
        private final Object original;
        private final Object converted;
        private final Object convertedAndExtracted;
        private final boolean isNewMessage;

        PolledMessage(String message, Object lastConvertedMessage, Function<String, Object> converter, DataNodeId nodeToExtract) {
            isNewMessage = message != null;
            original = message;
            converted = message == null ?
                    lastConvertedMessage :
                    converter.apply(message);

            convertedAndExtracted = ValueExtractorByPath.extractFromMapOrList(converted, nodeToExtract.getPath());
        }
    }
}
