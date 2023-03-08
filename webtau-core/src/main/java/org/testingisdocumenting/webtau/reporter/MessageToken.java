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
        return MessageTokenToMapConverter.convert(this);
    }

    public boolean isPrettyPrintValue() {
        return type.equals(TokenizedMessage.TokenTypes.PRETTY_PRINT_VALUE.getType()) ||
                type.equals(TokenizedMessage.TokenTypes.PRETTY_PRINT_VALUE_FIRST_LINES.getType());
    }

    public boolean isDelimiter() {
        return type.equals(TokenizedMessage.TokenTypes.DELIMITER.getType()) || type.equals(TokenizedMessage.TokenTypes.DELIMITER_NO_AUTO_SPACING.getType());
    }

    public boolean isError() {
        return type.equals(TokenizedMessage.TokenTypes.ERROR.getType());
    }

    @Override
    public String toString() {
        return "MessageToken{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
