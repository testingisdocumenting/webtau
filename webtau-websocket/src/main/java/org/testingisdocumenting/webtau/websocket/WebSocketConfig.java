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

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;
import org.testingisdocumenting.webtau.expectation.timer.SystemTimerConfig;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.*;

public class WebSocketConfig implements WebTauConfigHandler {
    private static final ConfigValue webSocketPollTimeout = declare("webSocketPollTimeout", "poll new message timeout",
            () -> SystemTimerConfig.DEFAULT_WAIT_TIMEOUT);

    private static final ConfigValue webSocketMaxMessages = declare("webSocketMaxMessages", "max number of received messages to keep for polling/waiting",
            () -> 1000);

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(webSocketPollTimeout, webSocketMaxMessages);
    }

    public static long getWebSocketPollTimeout() {
        return webSocketPollTimeout.getAsLong();
    }

    public static int getWebSocketMaxMessages() {
        return webSocketMaxMessages.getAsInt();
    }
}
