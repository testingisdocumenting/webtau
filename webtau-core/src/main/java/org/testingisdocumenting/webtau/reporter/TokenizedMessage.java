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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TokenizedMessage implements Iterable<MessageToken> {
    private final List<MessageToken> tokens;

    public TokenizedMessage() {
        tokens = new ArrayList<>();
    }

    public static TokenizedMessage tokenizedMessage(MessageToken... tokens) {
        TokenizedMessage message = new TokenizedMessage();
        message.add(tokens);

        return message;
    }

    public static TokenizedMessage tokenizedMessage(TokenizedMessage tokenizedMessage) {
        TokenizedMessage message = new TokenizedMessage();
        message.add(tokenizedMessage);

        return message;
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
                    .equals(IntegrationTestsMessageBuilder.TokenTypes.DELIMITER.getType());

            result.append(token.getValue());
            if (!isNextDelimiter && !isLast) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}
