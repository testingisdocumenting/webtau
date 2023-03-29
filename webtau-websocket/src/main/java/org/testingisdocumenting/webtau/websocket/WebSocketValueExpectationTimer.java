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

import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.time.Time;

public class WebSocketValueExpectationTimer implements ExpectationTimer {
    private long startTime;
    private final WebSocketMessageListener messageListener;

    public WebSocketValueExpectationTimer(WebSocketMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void start() {
        startTime = Time.currentTimeMillis();
    }

    @Override
    public void tick(long millis) {
        if (!messageListener.getMessages().isEmpty()) {
            return;
        }

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasTimedOut(long millis) {
        return (Time.currentTimeMillis() - startTime) > millis;
    }
}
