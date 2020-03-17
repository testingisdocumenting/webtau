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

import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.stream.Stream;

public class ConsoleStepReporter implements StepReporter {
    private TokenizedMessageToAnsiConverter toAnsiConverter;

    public ConsoleStepReporter(TokenizedMessageToAnsiConverter toAnsiConverter) {
        this.toAnsiConverter = toAnsiConverter;
    }

    @Override
    public void onStepStart(TestStep step) {
        ConsoleOutputs.out(Stream.concat(Stream.of(
                createIndentation(step.getNumberOfParents()),
                Color.YELLOW, "> "), toAnsiConverter.convert(step.getInProgressMessage()).stream()).toArray());
    }

    @Override
    public void onStepSuccess(TestStep step) {
        TokenizedMessage completionMessage = step.getCompletionMessage();

        int numberOfParents = step.getNumberOfParents();

        TokenizedMessage completionMessageToUse = isLastTokenMatcher(completionMessage) ?
                completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 1)
                        .add(reAlignText(numberOfParents + 2, completionMessage.getLastToken())) :
                completionMessage;

        ConsoleOutputs.out(
                Stream.concat(
                        Stream.concat(Stream.of(createIndentation(numberOfParents), Color.GREEN, ". "),
                                toAnsiConverter.convert(completionMessageToUse).stream()),
                        Stream.of(Color.YELLOW, " (", Color.GREEN, renderTimeTaken(step), Color.YELLOW, ')')).toArray());
    }

    private String renderTimeTaken(TestStep step) {
        long seconds = step.getElapsedTime() / 1000;
        long millisLeft = step.getElapsedTime() % 1000;

        return (seconds > 0 ? String.valueOf(seconds) + "s ": "") +
                String.valueOf(millisLeft) + "ms";
    }

    @Override
    public void onStepFailure(TestStep step) {
        TokenizedMessage completionMessageToUse = messageTokensForFailedStep(step);

        ConsoleOutputs.out(Stream.concat(Stream.of(
                createIndentation(step.getNumberOfParents()),
                Color.RED, "X "),
                toAnsiConverter.convert(completionMessageToUse).stream()).toArray());
    }

    private TokenizedMessage messageTokensForFailedStep(TestStep step) {
        TokenizedMessage completionMessage = step.getCompletionMessage();
        int numberOfParents = step.getNumberOfParents();

        boolean isLastTokenError = isLastTokenError(completionMessage);
        if (!isLastTokenError) {
            return completionMessage;
        }

        if (step.hasFailedChildrenSteps()) {
            // we don't render children errors one more time in case this step has failed children steps
            // last two tokens of a message are delimiter and error tokens
            // so we remove them
            return completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 2);
        }

        return completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 1)
                .add(reAlignText(numberOfParents + 2, completionMessage.getLastToken()));
    }

    private MessageToken reAlignText(int indentLevel, MessageToken token) {
        if (token.getValue() == null) {
            return token;
        }

        String text = token.getValue().toString();

        return new MessageToken(token.getType(),
                StringUtils.indentAllLinesButFirst(createIndentation(indentLevel), text));
    }

    private boolean isLastTokenMatcher(TokenizedMessage completionMessage) {
        return completionMessage.getLastToken().getType().equals(
                IntegrationTestsMessageBuilder.TokenTypes.MATCHER.getType());
    }

    private boolean isLastTokenError(TokenizedMessage completionMessage) {
        return completionMessage.getLastToken().getType().equals(
                IntegrationTestsMessageBuilder.TokenTypes.ERROR.getType());
    }

    private String createIndentation(int indentation) {
        return StringUtils.createIndentation(indentation * 2);
    }
}
