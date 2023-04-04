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

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class WebSocketSpringBootTest {
    @Test
    public void waitUntilReceiveMessage() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "IBM"));

        wsSession.received.waitTo(equal(map(
                "price", greaterThan(100),
                "symbol", "IBM")));
    }

    @Test
    public void pollMessage() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "DUMMY"));

        String messageOne = wsSession.received.pollAsText();
        actual(messageOne).should(equal("{\"symbol\":\"DUMMY\",\"price\":0}"));

        String messageTwo = wsSession.received.pollAsText(10);
        actual(messageTwo).should(equal(null));
    }

    @Test
    public void waitMessageSecondTimeShouldNotUseCachedValue() {
        WebSocketSession wsSession = websocket.connect("/prices");
        wsSession.send(map("symbol", "DUMMY"));

        wsSession.received.waitTo(equal(map("symbol", "DUMMY", "price", 0)));
        wsSession.received.should(equal(null));
        wsSession.received.waitTo(equal(null));
    }
}
