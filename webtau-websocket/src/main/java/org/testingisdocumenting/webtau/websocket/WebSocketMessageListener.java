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

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@ClientEndpoint
public class WebSocketMessageListener {
    private final int MAX_MESSAGES_TO_KEEP = 500;

    private final BlockingQueue<String> messages;

    public WebSocketMessageListener() {
        messages = new ArrayBlockingQueue<>(MAX_MESSAGES_TO_KEEP);
    }

    public BlockingQueue<String> getMessages() {
        return messages;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (messages.size() == MAX_MESSAGES_TO_KEEP) {
            messages.remove();
        }

        messages.add(message);
    }
}
