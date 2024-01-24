/*
 * Copyright 2022 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public record ValuePathMessage(ValuePath actualPath, Supplier<TokenizedMessage> messageFunc) {
    public TokenizedMessage buildFullMessage() {
        return tokenizedMessage().id(actualPath.getPath()).colon().addWithIndentation(messageFunc.get());
    }

    public TokenizedMessage buildMessage() {
        return messageFunc.get();
    }

    @Override
    public String toString() {
        return buildFullMessage().toString();
    }
}
