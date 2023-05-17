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

import java.net.http.WebSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebSocketMessageListener implements WebSocket.Listener {
    private final BlockingQueue<String> messages;
    private final StringBuilder partialMessage;
    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    public WebSocketMessageListener() {
        messages = new ArrayBlockingQueue<>(WebSocketConfig.getWebSocketMaxMessages());
        partialMessage = new StringBuilder();
    }

    public boolean isClosed() {
        return isClosed.get();
    }

    public BlockingQueue<String> getMessages() {
        return messages;
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        partialMessage.append(data);
        if (!last) {
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        if (messages.size() == WebSocketConfig.getWebSocketMaxMessages()) {
            messages.remove();
        }

        messages.add(partialMessage.toString());
        partialMessage.setLength(0);

        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        isClosed.set(true);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }
}
