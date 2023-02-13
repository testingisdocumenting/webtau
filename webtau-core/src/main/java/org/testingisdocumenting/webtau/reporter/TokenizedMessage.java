/*
 * Copyright 2021 webtau maintainers
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

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class TokenizedMessage implements Iterable<MessageToken> {
    public enum TokenTypes {
        ACTION("action"),
        ERROR("error"),
        WARNING("warning"),
        ID("id"),
        CLASSIFIER("classifier"),
        MATCHER("matcher"),
        STRING_VALUE("stringValue"),
        QUERY_VALUE("queryValue"),
        NUMBER_VALUE("numberValue"),
        PRETTY_PRINT_VALUE("pretty_print_value"),
        PRETTY_PRINT_VALUE_FIRST_LINES("pretty_print_value_first_lines"),
        URL_VALUE("url"),
        SELECTOR_TYPE("selectorType"),
        SELECTOR_VALUE("selectorValue"),
        PREPOSITION("preposition"),
        DELIMITER("delimiter"),
        NONE("none");

        private final String type;

        TokenTypes(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public MessageToken token(Object value) {
            return new MessageToken(type, value);
        }
    }

    private final List<MessageToken> tokens;

    public TokenizedMessage() {
        tokens = new ArrayList<>();
    }

    public TokenizedMessage add(String type, Object value) {
        return add(new MessageToken(type, value));
    }

    public TokenizedMessage add(MessageToken... tokens) {
        this.tokens.addAll(Arrays.stream(tokens).filter(t -> !t.isEmpty()).collect(toList()));
        return this;
    }

    public TokenizedMessage add(List<MessageToken> tokens) {
        this.tokens.addAll(tokens);
        return this;
    }

    public TokenizedMessage add(TokenizedMessage tokenizedMessage) {
        tokenizedMessage.tokensStream().forEach(this::add);
        return this;
    }

    public TokenizedMessage action(String label) {
        return add(TokenTypes.ACTION.token(label));
    }

    public TokenizedMessage error(String message) {
        return add(TokenTypes.ERROR.token(message));
    }

    public TokenizedMessage warning(String message) {
        return add(TokenTypes.WARNING.token(message));
    }

    public TokenizedMessage id(String id) {
        return add(TokenTypes.ID.token(id));
    }

    public TokenizedMessage classifier(String label) {
        return add(TokenTypes.CLASSIFIER.token(label));
    }

    public TokenizedMessage matcher(String label) {
        return add(TokenTypes.MATCHER.token(label));
    }

    public TokenizedMessage string(Object value) {
        return add(TokenTypes.STRING_VALUE.token(value.toString()));
    }

    public TokenizedMessage query(String query) {
        return add(TokenTypes.QUERY_VALUE.token(query));
    }

    public TokenizedMessage number(Number number) {
        return add(TokenTypes.NUMBER_VALUE.token(number));
    }

    public TokenizedMessage value(Object value) {
        return add(TokenTypes.PRETTY_PRINT_VALUE.token(value));
    }

    public TokenizedMessage valueFirstLinesOnly(Object value) {
        return add(TokenTypes.PRETTY_PRINT_VALUE_FIRST_LINES.token(value));
    }

    public TokenizedMessage url(String url) {
        return add(TokenTypes.URL_VALUE.token(url));
    }

    public TokenizedMessage url(Path url) {
        return add(TokenTypes.URL_VALUE.token(url.toString()));
    }

    public TokenizedMessage selectorType(String type) {
        return add(TokenTypes.SELECTOR_TYPE.token(type));
    }

    public TokenizedMessage selectorValue(String value) {
        return add(TokenTypes.SELECTOR_VALUE.token(value));
    }

    public TokenizedMessage preposition(String preposition) {
        return add(TokenTypes.PREPOSITION.token(preposition));
    }

    public TokenizedMessage delimiter(String delimiter) {
        return add(TokenTypes.DELIMITER.token(delimiter));
    }

    public TokenizedMessage to() {
        return add(TokenTypes.PREPOSITION.token("to"));
    }

    public TokenizedMessage of() {
        return add(TokenTypes.PREPOSITION.token("of"));
    }

    public TokenizedMessage forP() {
        return add(TokenTypes.PREPOSITION.token("for"));
    }

    public TokenizedMessage from() {
        return add(TokenTypes.PREPOSITION.token("from"));
    }

    public TokenizedMessage over() {
        return add(TokenTypes.PREPOSITION.token("over"));
    }

    public TokenizedMessage as() {
        return add(TokenTypes.PREPOSITION.token("as"));
    }

    public TokenizedMessage into() {
        return add(TokenTypes.PREPOSITION.token("into"));
    }

    public TokenizedMessage on() {
        return add(TokenTypes.PREPOSITION.token("on"));
    }

    public TokenizedMessage with() {
        return add(TokenTypes.PREPOSITION.token("with"));
    }

    public TokenizedMessage comma() {
        return add(TokenTypes.DELIMITER.token(","));
    }

    public TokenizedMessage colon() {
        return add(TokenTypes.DELIMITER.token(":"));
    }

    public TokenizedMessage none(String label) {
        return add(TokenTypes.NONE.token(label));
    }

    public TokenizedMessage subMessage(int from, int toExclusive) {
        TokenizedMessage messageTokens = new TokenizedMessage();
        messageTokens.add(tokens.subList(from, toExclusive));

        return messageTokens;
    }

    public int getNumberOfTokens() {
        return tokens.size();
    }

    public MessageToken getTokenAtIdx(int idx) {
        return tokens.get(idx);
    }

    public MessageToken getLastToken() {
        return tokens.get(tokens.size() - 1);
    }

    public Stream<MessageToken> tokensStream() {
        return tokens.stream();
    }

    public List<Map<String, ?>> toListOfMaps() {
        return tokens.stream().map(MessageToken::toMap).collect(toList());
    }

    @Override
    public Iterator<MessageToken> iterator() {
        return tokens.iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int idx = 0; idx < tokens.size(); idx++) {
            MessageToken token = tokens.get(idx);
            boolean isLast = idx == tokens.size() - 1;
            boolean isNextDelimiter = !isLast && tokens.get(idx + 1).getType()
                    .equals(TokenTypes.DELIMITER.getType());

            result.append(token.getValue());
            if (!isNextDelimiter && !isLast) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}
