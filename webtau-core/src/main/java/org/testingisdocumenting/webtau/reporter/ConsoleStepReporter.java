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

import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.IndentedConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConsoleStepReporter implements StepReporter {
    private final TokenizedMessageToAnsiConverter toAnsiConverter;
    private final Supplier<Integer> verboseLevelSupplier;

    public ConsoleStepReporter(TokenizedMessageToAnsiConverter toAnsiConverter, Supplier<Integer> verboseLevelSupplier) {
        this.toAnsiConverter = toAnsiConverter;
        this.verboseLevelSupplier = verboseLevelSupplier;
    }

    @Override
    public void onStepStart(WebTauStep step) {
        executeIfWithinVerboseLevel(step, () -> printStepStart(step));
    }

    @Override
    public void onStepSuccess(WebTauStep step) {
        executeIfWithinVerboseLevel(step, () -> printStepSuccess(step));
    }

    @Override
    public void onStepFailure(WebTauStep step) {
        executeIfWithinVerboseLevel(step, () -> printStepFailure(step));
    }

    @Override
    public void onStepRepeatStart(WebTauStep step, int current, int total) {
        executeIfWithinVerboseLevel(step, () -> printStepRepeatStart(step, current, total));
    }

    @Override
    public void onStepRepeatSuccess(WebTauStep step, int current, int total) {
        executeIfWithinVerboseLevel(step, () -> printStepRepeatSuccess(step, current, total));
    }

    @Override
    public void onStepRepeatFailure(WebTauStep step, int current, int total) {
        executeIfWithinVerboseLevel(step, () -> printStepRepeatFailure(step, current, total));
    }

    private void printStepStart(WebTauStep step) {
        ConsoleOutputs.out(
                Stream.concat(
                        Stream.concat(
                                stepStartBeginningStream(step),
                                personaStream(step)),
                        toAnsiConverter.convert(step.getInProgressMessage()).stream()
                ).toArray());

        printStepInput(step);
    }

    private void printStepSuccess(WebTauStep step) {
        TokenizedMessage completionMessage = step.getCompletionMessage();

        TokenizedMessage completionMessageToUse = isLastTokenMatcher(completionMessage) ?
                completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 1)
                        .add(reAlignText(step.getNumberOfParents() + 2, completionMessage.getLastToken())) :
                completionMessage;

        printStepOutput(step);

        ConsoleOutputs.out(Stream.concat(Stream.concat(Stream.concat(stepSuccessBeginningStream(step), personaStream(step)),
                toAnsiConverter.convert(completionMessageToUse).stream()),
                timeTakenTokenStream(step)).toArray());
    }

    private void printStepFailure(WebTauStep step) {
        TokenizedMessage completionMessageToUse = messageTokensForFailedStep(step);

        printStepOutput(step);

        ConsoleOutputs.out(Stream.concat(Stream.concat(Stream.concat(stepFailureBeginningStream(step), personaStream(step)),
                toAnsiConverter.convert(completionMessageToUse).stream()),
                timeTakenTokenStream(step)).toArray());
    }

    private void printStepRepeatStart(WebTauStep step, int currentIdx, int total) {
        ConsoleOutputs.out(Stream.concat(stepStartBeginningStream(step),
                stepCurrentIdxOfTotalStream(currentIdx, total)).toArray());
    }

    private void printStepRepeatSuccess(WebTauStep step, int currentIdx, int total) {
        ConsoleOutputs.out(Stream.concat(stepSuccessBeginningStream(step),
                Stream.concat(
                        stepCurrentIdxOfTotalStream(currentIdx, total),
                        timeTakenTokenStream(step))).toArray());
    }

    private void printStepRepeatFailure(WebTauStep step, int currentIdx, int total) {
        printStepFailure(step);
    }

    private Stream<Object> stepStartBeginningStream(WebTauStep step) {
        return Stream.of(createIndentation(step.getNumberOfParents()), Color.YELLOW, "> ");
    }

    private Stream<Object> stepSuccessBeginningStream(WebTauStep step) {
        return Stream.of(createIndentation(step.getNumberOfParents()), Color.GREEN, ". ");
    }

    private Stream<Object> stepFailureBeginningStream(WebTauStep step) {
        return Stream.of(createIndentation(step.getNumberOfParents()), Color.RED, "X ");
    }

    private Stream<Object> stepCurrentIdxOfTotalStream(int currentIdx, int total) {
        return Stream.of(Color.BLUE, currentIdx + 1, Color.YELLOW, "/", Color.BLUE, total);
    }

    private Stream<Object> timeTakenTokenStream(WebTauStep step) {
        return Stream.of(Color.YELLOW, " (", Color.GREEN, renderTimeTaken(step), Color.YELLOW, ')');
    }

    private String renderTimeTaken(WebTauStep step) {
        long seconds = step.getElapsedTime() / 1000;
        long millisLeft = step.getElapsedTime() % 1000;

        return (seconds > 0 ? seconds + "s " : "") +
                millisLeft + "ms";
    }

    private void printStepInput(WebTauStep step) {
        if (skipRenderRequestResponse()) {
            return;
        }

        step.getInput().prettyPrint(createIndentedConsoleOutput(step));
    }

    private void printStepOutput(WebTauStep step) {
        if (skipRenderRequestResponse()) {
            return;
        }

        step.getOutput().prettyPrint(createIndentedConsoleOutput(step));
    }

    private IndentedConsoleOutput createIndentedConsoleOutput(WebTauStep step) {
        return new IndentedConsoleOutput(ConsoleOutputs.asCombinedConsoleOutput(),
                numberOfSpacedForIndentLevel(step.getNumberOfParents() + 1));
    }

    private boolean skipRenderRequestResponse() {
        return verboseLevelSupplier.get() <= WebTauStep.getCurrentStep().getNumberOfParents() + 1;
    }

    private TokenizedMessage messageTokensForFailedStep(WebTauStep step) {
        TokenizedMessage completionMessage = step.getCompletionMessage();
        int numberOfParents = step.getNumberOfParents();

        boolean isLastTokenError = isLastTokenError(completionMessage);
        if (!isLastTokenError) {
            return completionMessage;
        }

        if (step.hasFailedChildrenSteps() && !skipRenderRequestResponse()) {
            // we don't render children errors one more time in case this step has failed children steps
            // last two tokens of a message are delimiter and error tokens
            // so we remove them
            return completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 2);
        }

        return completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 1)
                .add(reAlignText(numberOfParents + 2, completionMessage.getLastToken()));
    }

    private Stream<Object> personaStream(WebTauStep step) {
        if (step.getPersonaId().isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(Color.YELLOW, step.getPersonaId(), " ", Color.RESET);
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

    private String createIndentation(int indentLevel) {
        return StringUtils.createIndentation(numberOfSpacedForIndentLevel(indentLevel));
    }

    private int numberOfSpacedForIndentLevel(int indentLevel) {
        return indentLevel * 2;
    }

    private void executeIfWithinVerboseLevel(WebTauStep step, Runnable code) {
        int currentLevel = step.getNumberOfParents() + 1;
        if (currentLevel <= verboseLevelSupplier.get()) {
            code.run();
        }
    }
}
