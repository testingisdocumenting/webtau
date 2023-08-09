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

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.render.PrettyPrinterLine;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage.TokenTypes;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TokenizedMessageToAnsiConverter {
    public static final TokenizedMessageToAnsiConverter DEFAULT = new TokenizedMessageToAnsiConverter();
    private static final int PRETTY_PRINT_NUMBER_OF_LINES_LIMIT = 5;

    private final Map<String, TokenRenderDetails> tokenRenderDetails;
    private int firstLinePrefixWidth;

    public TokenizedMessageToAnsiConverter() {
        tokenRenderDetails = new HashMap<>();
        associateDefaultTokens();
    }

    public void associate(String tokenType, Object... ansiSequence) {
        tokenRenderDetails.put(tokenType, new TokenRenderDetails(Arrays.asList(ansiSequence)));
    }

    public List<Object> convert(ValueConverter valueConverter, TokenizedMessage tokenizedMessage, int firstLinePrefixWidth) {
        this.firstLinePrefixWidth = firstLinePrefixWidth;
        PrettyPrinterLine line = new PrettyPrinterLine();

        TokenRenderDetails previousTokenRenderDetails = null;
        int len = tokenizedMessage.getNumberOfTokens();
        for (int idx = 0; idx < len; idx++) {
            MessageToken messageToken = tokenizedMessage.getTokenAtIdx(idx);
            TokenRenderDetails renderDetails = this.tokenRenderDetails.get(messageToken.type());

            if (renderDetails == null) {
                throw new RuntimeException("no render details found for token: " + messageToken);
            }

            boolean isNextDelimiter = ((idx + 1) < len) && tokenizedMessage.getTokenAtIdx(idx + 1).isDelimiter();
            boolean isLast = (idx == len - 1);
            boolean addSpace = !isLast && !isNextDelimiter && !isDelimiterNoAutoSpacing(tokenizedMessage.getTokenAtIdx(idx));

            Stream<?> ansiSequence = convertToAnsiSequence(valueConverter, line, renderDetails, messageToken);
            if (addSpace) {
                ansiSequence = Stream.concat(ansiSequence, Stream.of(" "));
            }

            if (previousTokenRenderDetails != null && previousTokenRenderDetails.hasBold() && !renderDetails.startsWithBold()) {
                ansiSequence = Stream.concat(Stream.of(FontStyle.RESET), ansiSequence);
            }

            line.appendStream(ansiSequence);

            previousTokenRenderDetails = renderDetails;
        }

        return line.getStyleAndValues();
    }

    private boolean isDelimiterNoAutoSpacing(MessageToken token) {
        return token.type().equals(TokenTypes.DELIMITER_NO_AUTO_SPACING.getType());
    }

    private Stream<?> convertToAnsiSequence(ValueConverter valueConverter, PrettyPrinterLine currentLine, TokenRenderDetails renderDetails, MessageToken messageToken) {
        boolean usePrettyPrintFirstLinesOnly = messageToken.type().equals(TokenTypes.PRETTY_PRINT_VALUE_FIRST_LINES.getType());
        boolean usePrettyPrint = messageToken.type().equals(TokenTypes.PRETTY_PRINT_VALUE.getType());
        if (usePrettyPrint || usePrettyPrintFirstLinesOnly) {
            return ansiSequenceFromPrettyPrinter(valueConverter, currentLine, messageToken.value(), usePrettyPrintFirstLinesOnly);
        }

        Stream<Object> valueStream = Stream.of(messageToken.value());
        return Stream.concat(renderDetails.ansiSequence.stream(), valueStream);
    }

    private Stream<?> ansiSequenceFromPrettyPrinter(ValueConverter valueConverter, PrettyPrinterLine currentLine, Object value, boolean printFirstLinesOnly) {
        PrettyPrinter printer = new PrettyPrinter(0);
        printer.setValueConverter(valueConverter);
        printer.printObject(value);
        printer.flushCurrentLine();

        Stream<Object> result = Stream.empty();

        String indentation = StringUtils.createIndentation(currentLine.hasNewLine() ?
                currentLine.findWidthOfTheLastEffectiveLine():
                currentLine.findWidthOfTheLastEffectiveLine() + firstLinePrefixWidth);

        int numberOfLinesToPrint = printFirstLinesOnly ?
                Math.min(printer.getNumberOfLines(), PRETTY_PRINT_NUMBER_OF_LINES_LIMIT):
                printer.getNumberOfLines();

        for (int idx = 0; idx < numberOfLinesToPrint; idx++) {
            boolean isFirstLine = idx == 0;
            boolean isLastLine = idx == printer.getNumberOfLines() - 1;
            PrettyPrinterLine line = printer.getLine(idx);

            if (isFirstLine) {
                result = Stream.concat(result, line.getStyleAndValues().stream());
            } else {
                result = Stream.concat(result, Stream.concat(
                        Stream.of(indentation),
                        line.getStyleAndValues().stream()));
            }

            if (!isLastLine) {
                result = Stream.concat(result, Stream.of("\n"));
            }
        }

        if (numberOfLinesToPrint > 1 && numberOfLinesToPrint != printer.getNumberOfLines()) {
            result = Stream.concat(result, Stream.of(PrettyPrinter.DELIMITER_COLOR, indentation, "..."));
        }

        return result;
    }

    private void associateDefaultTokens() {
        associate(TokenTypes.ACTION.getType(), Color.BLUE);
        associate(TokenTypes.ERROR.getType(), Color.RED);
        associate(TokenTypes.WARNING.getType(), Color.YELLOW);
        associate(TokenTypes.ID.getType(), FontStyle.RESET, FontStyle.BOLD);
        associate(TokenTypes.CLASSIFIER.getType(), Color.CYAN);
        associate(TokenTypes.MATCHER.getType(), Color.RESET, Color.BLUE);
        associate(TokenTypes.STRING_VALUE.getType(), Color.GREEN);
        associate(TokenTypes.QUERY_VALUE.getType(), Color.YELLOW);
        associate(TokenTypes.NUMBER_VALUE.getType(), Color.BLUE);
        associate(TokenTypes.PRETTY_PRINT_VALUE.getType(), Color.RESET);
        associate(TokenTypes.PRETTY_PRINT_VALUE_FIRST_LINES.getType(), Color.RESET);
        associate(TokenTypes.URL_VALUE.getType(), Color.PURPLE);
        associate(TokenTypes.OBJECT_TYPE.getType(), Color.YELLOW);
        associate(TokenTypes.SELECTOR_TYPE.getType(), Color.PURPLE);
        associate(TokenTypes.SELECTOR_VALUE.getType(), FontStyle.BOLD, Color.PURPLE);
        associate(TokenTypes.PREPOSITION.getType(), Color.YELLOW);
        associate(TokenTypes.DELIMITER.getType(), Color.RESET);
        associate(TokenTypes.DELIMITER_NO_AUTO_SPACING.getType(), Color.RESET);
        associate(TokenTypes.NONE.getType(), Color.RESET);
    }

    private record TokenRenderDetails(List<Object> ansiSequence) {
        boolean hasBold() {
            return ansiSequence.stream().anyMatch(v -> v.equals(FontStyle.BOLD));
        }

        boolean startsWithBold() {
            return ansiSequence.stream().limit(1).anyMatch(v -> v.equals(FontStyle.BOLD));
        }
    }
}
