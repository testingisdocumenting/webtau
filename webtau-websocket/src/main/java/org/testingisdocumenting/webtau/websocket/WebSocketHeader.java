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

import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.testingisdocumenting.webtau.utils.StringUtils.*;

public class WebSocketHeader {
    private final Map<String, String> header;
    public static final WebSocketHeader EMPTY = new WebSocketHeader(Collections.emptyMap());

    WebSocketHeader(Map<String, String> header) {
        this.header = header;
    }

    WebSocketHeader() {
        this.header = new LinkedHashMap<>();
    }

    /**
     * Creates a new header from the current one with an additional key-value
     * @param firstKey first key
     * @param firstValue first value
     * @param restKv vararg key value sequence, e.g. "HEADER_ONE", "value_one", "HEADER_TWO", "value_two"
     * @return new header
     */
    public WebSocketHeader with(CharSequence firstKey, CharSequence firstValue, CharSequence... restKv) {
        Map<Object, Object> mapFromVararg = CollectionUtils.map(firstKey, firstValue, (Object[]) restKv);

        Map<String, String> copy = new LinkedHashMap<>(this.header);
        mapFromVararg.forEach((k, v) -> copy.put(toStringOrNull(k), toStringOrNull(v)));

        return new WebSocketHeader(copy);
    }

    /**
     * Creates a new header from the current one with an additional key values
     * @param additionalValues additional values
     * @return new header with combined values
     */
    public WebSocketHeader with(Map<CharSequence, CharSequence> additionalValues) {
        Map<String, String> copy = new LinkedHashMap<>(this.header);
        additionalValues.forEach((k, v) -> copy.put(toStringOrNull(k), toStringOrNull(v)));

        return new WebSocketHeader(copy);
    }

    public void forEachProperty(BiConsumer<String, String> consumer) {
        header.forEach((k, v) -> consumer.accept(toStringOrNull(k), toStringOrNull(v)));
    }
}
