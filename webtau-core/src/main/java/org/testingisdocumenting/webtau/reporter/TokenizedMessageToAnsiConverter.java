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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TokenizedMessageToAnsiConverter {
    private Map<String, TokenRenderDetails> tokenRenderDetails;

    public TokenizedMessageToAnsiConverter() {
        tokenRenderDetails = new HashMap<>();
    }

    public void associate(String tokenType, boolean isSpaceAfterRequired, Object... ansiSequence) {
        tokenRenderDetails.put(tokenType, new TokenRenderDetails(Arrays.asList(ansiSequence), isSpaceAfterRequired));
    }

    public List<Object> convert(TokenizedMessage tokenizedMessage) {
        List<Object> valuesAndStyles = new ArrayList<>();

        int i = 0;
        int len = tokenizedMessage.getNumberOfTokens();
        for (MessageToken messageToken : tokenizedMessage) {
            TokenRenderDetails renderDetails = this.tokenRenderDetails.get(messageToken.getType());

            if (renderDetails == null) {
                throw new RuntimeException("no render details found for token: " + messageToken);
            }

            boolean isLast = (i == len - 1);
            boolean addSpace = renderDetails.isSpaceAfterRequired && !isLast;
            Stream<?> ansiSequence = convertToAnsiSequence(renderDetails, messageToken, addSpace);

            ansiSequence.forEach(valuesAndStyles::add);
            i++;
        }

        return valuesAndStyles;
    }

    private Stream<?> convertToAnsiSequence(TokenRenderDetails renderDetails, MessageToken messageToken, boolean addSpace) {
        Stream<Object> valueStream = addSpace ?
                Stream.of(messageToken.getValue(), " "):
                Stream.of(messageToken.getValue());

        return Stream.concat(renderDetails.ansiSequence.stream(), valueStream);
    }

    private static class TokenRenderDetails {
        private List<Object> ansiSequence;
        private boolean isSpaceAfterRequired;

        public TokenRenderDetails(List<Object> ansiSequence, boolean isSpaceAfterRequired) {
            this.ansiSequence = ansiSequence;
            this.isSpaceAfterRequired = isSpaceAfterRequired;
        }
    }
}
