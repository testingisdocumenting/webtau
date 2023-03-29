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
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebSocketValue implements ActualValueExpectations, ActualValueAware, ActualPathAndDescriptionAware {
    private final String id;
    private final String destination;
    private final WebSocketMessageListener messageListener;

    public WebSocketValue(String id, String destination, WebSocketMessageListener messageListener) {
        this.id = id;
        this.destination = destination;
        this.messageListener = messageListener;
    }

    @Override
    public ExpectationTimer createExpectationTimer() {
        return new WebSocketValueExpectationTimer(messageListener);
    }

    @Override
    public ValuePath actualPath() {
        return new ValuePath(id);
    }

    @Override
    public TokenizedMessage describe() {
        return tokenizedMessage().id(id).from().url(destination);
    }

    @Override
    public Object actualValue() {
        String last = messageListener.getMessages().poll();
        if (last == null) {
            return null;
        }

        if (JsonUtils.looksLikeJson(last)) {
            try {
                return JsonUtils.deserialize(last);
            } catch (JsonParseException e) {
                return last;
            }
        }

        return last;
    }
}
