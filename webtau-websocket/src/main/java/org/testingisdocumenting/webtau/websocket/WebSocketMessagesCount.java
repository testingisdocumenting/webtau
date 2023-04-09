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

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebSocketMessagesCount implements ActualValueExpectations, ActualPathAndDescriptionAware, ActualValueAware {
    private final ActualPathAndDescriptionAware parentDescription;
    private final WebSocketMessageListener messageListener;
    private final ValuePath valuePath;

    public WebSocketMessagesCount(ActualPathAndDescriptionAware parentDescription, WebSocketMessageListener messageListener) {
        this.parentDescription = parentDescription;
        this.messageListener = messageListener;
        this.valuePath = new ValuePath("count");
    }

    @Override
    public ValuePath actualPath() {
        return valuePath;
    }

    public int get() {
        return messageListener.getMessages().size();
    }

    @Override
    public TokenizedMessage describe() {
        return tokenizedMessage().classifier("count").of().classifier("messages").add(parentDescription.describe());
    }

    @Override
    public Object actualValue() {
        return get();
    }

    @Override
    public ExpectationTimer createExpectationTimer() {
        return new WebSocketValueExpectationTimer(messageListener);
    }
}
