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

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.utils.StringUtils;

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
        PRETTY_PRINT_VALUE("prettyPrintValue"),
        PRETTY_PRINT_VALUE_FIRST_LINES("prettyPrintValueFirstLines"),
        URL_VALUE("url"),
        OBJECT_TYPE("objectType"),
        SELECTOR_TYPE("selectorType"),
        SELECTOR_VALUE("selectorValue"),
        PREPOSITION("preposition"),
        DELIMITER("delimiter"),
        DELIMITER_NO_AUTO_SPACING("delimiterNoAutoSpacing"),
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

    public static TokenizedMessage join(String delimiter, List<TokenizedMessage> messages) {
        TokenizedMessage result = new TokenizedMessage();
        int idx = 0;
        for (TokenizedMessage message : messages) {
            boolean isLast = idx == messages.size() - 1;
            result.add(message);
            if (!isLast) {
                result.delimiterNoAutoSpacing(delimiter);
            }

            idx++;
        }

        return result;
    }

    public TokenizedMessage add(String type, Object value) {
        return add(new MessageToken(type, value));
    }

    public TokenizedMessage add(MessageToken... tokens) {
        this.tokens.addAll(Arrays.stream(tokens).flatMap(this::splitTokenNewLines).toList());
        return this;
    }

    public TokenizedMessage addInFront(MessageToken... tokens) {
        this.tokens.addAll(0, Arrays.stream(tokens).flatMap(this::splitTokenNewLines).toList());
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

    public TokenizedMessage createReindentCopy(String indentation) {
        TokenizedMessage result = new TokenizedMessage();
        result.addWithIndentation(this, indentation);
        result.addInFront(TokenTypes.DELIMITER_NO_AUTO_SPACING.token(indentation));

        return result;
    }

    public TokenizedMessage addWithIndentation(TokenizedMessage tokenizedMessage) {
        int currentWidth = toString().length(); // only works if currently holds a single line
        if (!tokenizedMessage.isEmpty() && !tokenizedMessage.getFirstToken().type().equals(TokenTypes.DELIMITER_NO_AUTO_SPACING.getType())) {
            currentWidth += 1;
        }

        String indentation = StringUtils.createIndentation(currentWidth);
        return addWithIndentation(tokenizedMessage, indentation);
    }

    public TokenizedMessage addWithIndentation(TokenizedMessage tokenizedMessage, String indentation) {
        int idx = 0;
        for (MessageToken tokenToAdd : tokenizedMessage.tokens) {
            boolean isLastToken = idx == tokenizedMessage.tokens.size();

            add(tokenToAdd);

            if (!tokenToAdd.isPrettyPrintValue() && tokenToAdd.value().equals("\n")) {
                if (!isLastToken) {
                    add(new MessageToken(TokenTypes.DELIMITER_NO_AUTO_SPACING.getType(), indentation));
                }
            }

            idx++;
        }

        return this;
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
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

    public TokenizedMessage objectType(String type) {
        return add(TokenTypes.OBJECT_TYPE.token(type));
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

    public TokenizedMessage delimiterNoAutoSpacing(String delimiter) {
        return add(TokenTypes.DELIMITER_NO_AUTO_SPACING.token(delimiter));
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

    public TokenizedMessage newLine() {
        return add(TokenTypes.DELIMITER_NO_AUTO_SPACING.token("\n"));
    }

    public TokenizedMessage doubleNewLine() {
        return add(TokenTypes.DELIMITER_NO_AUTO_SPACING.token("\n\n"));
    }

    public TokenizedMessage none(String label) {
        return add(TokenTypes.NONE.token(label));
    }

    public TokenizedMessage subMessage(int from, int toExclusive) {
        TokenizedMessage messageTokens = new TokenizedMessage();
        messageTokens.add(tokens.subList(from, toExclusive));

        return messageTokens;
    }

    public boolean hasNewLineToken() {
        return tokens.stream().anyMatch(token -> "\n".equals(token.value()));
    }

    public boolean onlyErrorTokens() {
        return tokens.stream().allMatch(token -> TokenTypes.ERROR.getType().equals(token.type()));
    }

    public int getNumberOfTokens() {
        return tokens.size();
    }

    public MessageToken getTokenAtIdx(int idx) {
        return tokens.get(idx);
    }

    public MessageToken getFirstToken() {
        return tokens.get(0);
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
        List<Object> stylesAndValues = TokenizedMessageToAnsiConverter.DEFAULT.convert(ValueConverter.EMPTY, this, 0);
        return stylesAndValues.stream()
                .filter(v -> !(v instanceof Color) && !(v instanceof FontStyle))
                .map(Object::toString)
                .collect(joining(""));
    }

    private Stream<MessageToken> splitTokenNewLines(MessageToken token) {
        if (token.isPrettyPrintValue()) {
            return Stream.of(token);
        }

        if (token.value() == null) {
            return Stream.of(token);
        }

        String text = token.value().toString();
        if (!StringUtils.hasNewLineSeparator(text)) {
            return Stream.of(token);
        }

        String[] parts = StringUtils.splitLinesPreserveNewLineSeparator(text);
        List<MessageToken> result = new ArrayList<>();
        for (String part : parts) {
            if (part.equals("\n")) {
                result.add(new MessageToken(TokenTypes.DELIMITER_NO_AUTO_SPACING.getType(), part));
            } else {
                result.add(new MessageToken(token.type(), part));
            }
        }

        return result.stream();
    }
}
