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
    // we use _webtauInternal to avoid automatic groovy properties resolution and clash when using shortcut like resource.list[0].id
    private final String _webtauInternalLabel;
    private final ValuePath _webtauInternalValuePath;
    private final String _webtauInternalDestination;
    private final WebSocketMessageListener _webtauInternalMessageListener;
    private Object _webtauInternalLastConvertedMessage;

    private final DataNodeId _webtauInternalNodeToExtract;

    public final WebSocketMessagesCount count;

    public WebSocketMessages(String label, String destination, DataNodeId nodeToExtract, WebSocketMessageListener messageListener) {
        this._webtauInternalLabel = label;
        this._webtauInternalValuePath = new ValuePath(label);
        this._webtauInternalDestination = destination;
        this._webtauInternalNodeToExtract = nodeToExtract;
        this._webtauInternalMessageListener = messageListener;
        this.count = new WebSocketMessagesCount(this, messageListener);
    }

    /**
     * Use `get(path)` to narrow to a specific response value
     * @param path path to narrow to, e.g. "details.price"
     * @return new instance messages with updated path
     */
    public WebSocketMessages get(String path) {
        return new WebSocketMessages(_webtauInternalLabel, _webtauInternalDestination, _webtauInternalNodeToExtract.concat(path), _webtauInternalMessageListener);
    }

    /**
     * Use `get(idx)` to narrow to a specific response value from a list
     * @param idx idx to access to
     * @return new instance messages with updated path
     */
    public WebSocketMessages get(int idx) {
        return new WebSocketMessages(_webtauInternalLabel, _webtauInternalDestination, _webtauInternalNodeToExtract.peer(idx), _webtauInternalMessageListener);
    }

    /**
     * alias to {@link #get(int)} and a shortcut for Groovy DSL
     * @param idx index to get value at
     * @return new instance messages with updated path
     */
    public WebSocketMessages getAt(int idx) {
        return get(idx);
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
                () -> _webtauInternalMessageListener.getMessages().clear());

        step.execute(StepReportOptions.SKIP_START);
    }

    @Override
    public Object actualValue() {
        return actualValue(0, 0, 0);
    }

    @Override
    public Object actualValue(int attemptIdx, long tickMillis, long timeOutMillis) {
        if (attemptIdx == 0) {
            _webtauInternalLastConvertedMessage = null;
        }

        PolledMessage message = runPollMessageStep(_webtauInternalLastConvertedMessage, tickMillis, WebSocketUtils::convertToJsonIfPossible);
        _webtauInternalLastConvertedMessage = message.converted;

        return message.convertedAndExtracted;
    }

    @Override
    public ValuePath actualPath() {
        return _webtauInternalValuePath;
    }

    @Override
    public TokenizedMessage describe() {
        DataNodeId id = new DataNodeId(_webtauInternalLabel).concat(_webtauInternalNodeToExtract.getPath());
        return tokenizedMessage().id(id.getPath()).from().url(_webtauInternalDestination);
    }

    @Override
    public ExpectationTimer createExpectationTimer() {
        return new WebSocketValueExpectationTimer(_webtauInternalMessageListener);
    }

    private PolledMessage runPollMessageStep(Object lastConvertedMessage, long tickMillis, Function<String, Object> converter) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("polling").classifier("websocket message"),
                (result) -> ((PolledMessage) result).isNewMessage ?
                        tokenizedMessage().action("polled new message") :
                        tokenizedMessage().action("no new message is polled"),
                () -> {
                    try {
                        return new PolledMessage(_webtauInternalMessageListener.getMessages().poll(tickMillis, TimeUnit.MILLISECONDS), lastConvertedMessage, converter, _webtauInternalNodeToExtract);
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
