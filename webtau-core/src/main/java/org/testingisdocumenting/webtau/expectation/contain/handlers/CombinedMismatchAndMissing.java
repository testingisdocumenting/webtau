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

package org.testingisdocumenting.webtau.expectation.contain.handlers;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.ValuePathMessage;

import java.util.List;
import java.util.stream.Stream;

public record CombinedMismatchAndMissing(List<ValuePathMessage> mismatchMessages, List<ValuePathMessage> missingMessage) {
    int size() {
        return mismatchMessages.size() + missingMessage.size();
    }

    List<ValuePath> extractPaths() {
        return Stream.concat(
                mismatchMessages.stream().map(ValuePathMessage::actualPath),
                missingMessage.stream().map(ValuePathMessage::actualPath)).toList();
    }
}
