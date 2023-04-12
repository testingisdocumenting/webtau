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

package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;
import org.testingisdocumenting.webtau.websocket.WebSocketSession;

import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauDsl.*;
import static org.testingisdocumenting.webtau.websocket.WebSocket.websocket;

@WebTau
public class WebSocketSpringBootTest {
    @Test
    public void waitUntilReceiveMessage() {
        // connect-send-wait
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "IBM"));

        wsSession.received.waitTo(equal(map(
                "price", greaterThan(100),
                "symbol", "IBM")));
        wsSession.close();
        // connect-send-wait
    }

    @Test
    public void waitUntilReceiveMessageUsingPath() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "IBM"));

        // received-get
        wsSession.received.get("price").waitToBe(greaterThan(100));
        // received-get
        wsSession.close();
    }

    @Test
    public void waitUntilReceiveMessageUsingPathList() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "LIST"));

        // received-list
        wsSession.received.get("[2].price").waitToBe(greaterThan(30));
        // received-list
        wsSession.close();
    }

    @Test
    public void pollMessageAfterWait() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "IBM"));

        wsSession.received.waitTo(equal(map(
                "price", greaterThan(100),
                "symbol", "IBM")));

        // poll-after-wait
        String nextMessage = wsSession.received.pollAsText();
        actual(nextMessage).should(equal("{\"symbol\":\"IBM\",\"price\":102}"));

        String nextNextMessage = wsSession.received.pollAsText(100); // explicit timeout in milliseconds for new message to arrive
        actual(nextNextMessage).should(equal("{\"symbol\":\"IBM\",\"price\":103}"));
        // poll-after-wait

        wsSession.close();
    }

    @Test
    public void pollAsMap() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "IBM"));

        // poll-as-map
        Map<String, ?> message = wsSession.received.poll();
        actual(message.get("symbol")).should(equal("IBM"));
        // poll-as-map

        wsSession.close();
    }

    @Test
    public void pollDummyMessage() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "DUMMY"));

        String messageOne = wsSession.received.pollAsText();
        actual(messageOne).should(equal("{\"symbol\":\"DUMMY\",\"price\":0}"));

        String messageTwo = wsSession.received.pollAsText(10);
        actual(messageTwo).should(equal(null));

        wsSession.close();
    }

    @Test
    public void waitMessageSecondTimeShouldNotUseCachedValue() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "DUMMY"));

        wsSession.received.waitTo(equal(map("symbol", "DUMMY", "price", 0)));
        wsSession.received.should(equal(null));
        wsSession.received.waitTo(equal(null));
        wsSession.close();
    }

    @Test
    public void discardMessages() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "IBM"));

        wsSession.received.count.waitTo(equal(53));

        // discard-poll
        wsSession.received.discard();

        String nextMessage = wsSession.received.pollAsText(1);
        actual(nextMessage).should(equal(null));
        // discard-poll

        wsSession.close();
    }
}
