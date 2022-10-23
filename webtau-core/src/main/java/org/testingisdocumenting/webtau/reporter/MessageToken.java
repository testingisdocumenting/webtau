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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Map;

public class MessageToken {
    private final String type;
    private final Object value;

    public MessageToken(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Map<String, ?> toMap() {
        return CollectionUtils.aMapOf("type", type, "value", value);
    }

    public boolean isEmpty() {
        return value == null || value.toString().isEmpty();
    }

    @Override
    public String toString() {
        return "MessageToken{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
