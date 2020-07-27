/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page;

import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.function.Function;
import java.util.function.Supplier;

public interface PageElementStepExecutor {
    void execute(TokenizedMessage inProgressMessage,
                 Function<Object, TokenizedMessage> completionMessageFunc,
                 Supplier<Object> action);

    default void execute(TokenizedMessage inProgressMessage,
                 Supplier<TokenizedMessage> completionMessageSupplier,
                 Runnable action) {
        execute(inProgressMessage, (arg) -> completionMessageSupplier.get(), () -> {
            action.run();
            return null;
        });
    }
}
