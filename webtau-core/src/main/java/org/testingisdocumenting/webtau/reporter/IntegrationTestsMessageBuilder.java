/*
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

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;

import java.util.Arrays;

public class IntegrationTestsMessageBuilder {
    public enum TokenTypes {
        ERROR("error", Color.RED),
        NONE("none", FontStyle.NORMAL),
        ACTION("action", Color.BLUE),
        ID("id", FontStyle.BOLD),
        CLASSIFIER("classifier", Color.CYAN),
        MATCHER("matcher", Color.GREEN),
        STRING_VALUE("stringValue", Color.GREEN),
        URL("url", Color.PURPLE),
        SELECTOR_TYPE("selectorType", Color.PURPLE),
        SELECTOR_VALUE("selectorValue", FontStyle.BOLD, Color.PURPLE),
        PREPOSITION("preposition", Color.BLACK),
        DELIMITER("delimiter", Color.WHITE);

        private final String type;
        private final boolean delimiterAfter;
        private final Object[] styles;

        TokenTypes(String type, Object... styles) {
            this(type, true, styles);
        }

        TokenTypes(String type, boolean delimiterAfter, Object... styles) {
            this.type = type;
            this.delimiterAfter = delimiterAfter;
            this.styles = styles;
        }

        public String getType() {
            return type;
        }

        public MessageToken token(Object value) {
            return new MessageToken(type, value);
        }
    }

    public static final MessageToken TO = TokenTypes.PREPOSITION.token("to");
    public static final MessageToken OF = TokenTypes.PREPOSITION.token("of");
    public static final MessageToken COMMA = TokenTypes.DELIMITER.token(",");

    private static final TokenizedMessageToAnsiConverter converter = createConverter();

    public static MessageToken id(String value) {
        return TokenTypes.ID.token(value);
    }

    public static MessageToken classifier(String value) {
        return TokenTypes.CLASSIFIER.token(value);
    }

    public static MessageToken stringValue(Object value) {
        return TokenTypes.STRING_VALUE.token(escapeSpecialChars(value.toString()));
    }

    public static MessageToken urlValue(String url) {
        return TokenTypes.URL.token(url);
    }

    public static MessageToken action(String action) {
        return TokenTypes.ACTION.token(action);
    }

    public static MessageToken matcher(String matcher) {
        return TokenTypes.MATCHER.token(matcher);
    }

    public static MessageToken none(String text) {
        return TokenTypes.NONE.token(text);
    }

    public static MessageToken selectorType(String selector) {
        return TokenTypes.SELECTOR_TYPE.token(selector);
    }

    public static MessageToken selectorValue(String selector) {
        return TokenTypes.SELECTOR_VALUE.token(selector);
    }

    public static TokenizedMessageToAnsiConverter getConverter() {
        return converter;
    }

    private static Object escapeSpecialChars(String text) {
        return text.replace("\n", "\\n");
    }

    private static TokenizedMessageToAnsiConverter createConverter() {
        TokenizedMessageToAnsiConverter c = new TokenizedMessageToAnsiConverter();
        Arrays.stream(TokenTypes.values()).forEach(t -> c.associate(t.type, t.delimiterAfter, t.styles));

        return c;
    }
}
