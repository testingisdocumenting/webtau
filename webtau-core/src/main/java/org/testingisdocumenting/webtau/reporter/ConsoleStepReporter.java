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
import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleUtils;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.utils.StringUtils;
import org.testingisdocumenting.webtau.utils.TimeUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

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
        executeIfWithinVerboseLevel(step, () -> printStepFailure(step, false));
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

    public boolean isWithinVerboseLevel(WebTauStep step) {
        int currentLevel = step.getNumberOfParents() + 1;
        return currentLevel <= verboseLevelSupplier.get();
    }

    public void printStepFailureFailedStepMessageFirst(WebTauStep step, boolean forceFailureDisplay) {
        printStepFailureWithoutOutput(step, forceFailureDisplay);
        printStepOutputs(step);
    }

    public void printStepFailureWithoutOutput(WebTauStep step, boolean forceFailureDisplay) {
        TokenizedMessage completionMessageToUse = messageTokensForFailedStep(step, forceFailureDisplay);
        List<Object> prefix = Stream.concat(stepFailureBeginningStream(step), personaStream(step))
                .collect(Collectors.toList());

        ConsoleOutputs.out(Stream.concat(Stream.concat(prefix.stream(),
                        toAnsiConverter.convert(step.getValueConverter(), completionMessageToUse, AnsiConsoleUtils.calcEffectiveWidth(prefix)).stream()),
                timeTakenTokenStream(step)).toArray());
    }

    public void printStepOutputs(WebTauStep step) {
        if (skipRenderInputOutput()) {
            return;
        }

        PrettyPrinter printer = createInputOutputPrettyPrinter(step);
        step.getOutputsStream().forEach(output -> output.prettyPrint(printer));
        printer.renderToConsole(ConsoleOutputs.asCombinedConsoleOutput());
    }

    private void printStepStart(WebTauStep step) {
        List<Object> prefix = Stream.concat(stepStartBeginningStream(step), personaStream(step))
                .collect(Collectors.toList());

        ConsoleOutputs.out(
                Stream.concat(
                        prefix.stream(),
                        toAnsiConverter.convert(step.getValueConverter(), step.getInProgressMessage(), AnsiConsoleUtils.calcEffectiveWidth(prefix)).stream()
                ).toArray());

        printStepInput(step);
    }

    private void printStepSuccess(WebTauStep step) {
        if (isTraceOrWarningStep(step)) {
            return;
        }

        TokenizedMessage completionMessage = step.getCompletionMessage();

        TokenizedMessage completionMessageToUse = isLastTokenMatcher(completionMessage) ?
                completionMessage.subMessage(0, completionMessage.getNumberOfTokens() - 1)
                        .add(reAlignText(step.getNumberOfParents() + 2, completionMessage.getLastToken())) :
                completionMessage;

        printStepOutputs(step);

        List<Object> messagePrefix = Stream.concat(stepSuccessBeginningStream(step), personaStream(step))
                .collect(Collectors.toList());

        ConsoleOutputs.out(Stream.concat(
                Stream.concat(
                        messagePrefix.stream(),
                        toAnsiConverter.convert(step.getValueConverter(), completionMessageToUse, AnsiConsoleUtils.calcEffectiveWidth(messagePrefix)).stream()),
                timeTakenTokenStream(step)).toArray());
    }

    private void printStepFailure(WebTauStep step, boolean forceFailureDisplay) {
        if (step.getClassifier().equals(WebTauStepClassifiers.MATCHER)) {
            printStepFailureFailedStepMessageFirst(step, forceFailureDisplay);
        } else {
            printStepOutputs(step);
            printStepFailureWithoutOutput(step, forceFailureDisplay);
        }
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
        printStepFailure(step, true);
    }

    private Stream<Object> stepStartBeginningStream(WebTauStep step) {
        if (isTraceStep(step)) {
            return Stream.of(createIndentation(step.getNumberOfParents()),
                    FontStyle.BOLD, "[tracing]", Color.RESET, " ");
        } else if (isWarningStep(step)) {
            return Stream.of(createIndentation(step.getNumberOfParents()),
                    Color.RED, "[warning]", Color.RESET, " ");
        }

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
        return TimeUtils.renderMillisHumanReadable(step.getElapsedTime());
    }

    private void printStepInput(WebTauStep step) {
        if (skipRenderInputOutput()) {
            return;
        }

        PrettyPrinter printer = createInputOutputPrettyPrinter(step);
        step.getInput().prettyPrint(printer);
        printer.renderToConsole(ConsoleOutputs.asCombinedConsoleOutput());
    }

    private PrettyPrinter createInputOutputPrettyPrinter(WebTauStep step) {
        return new PrettyPrinter(numberOfSpacesForIndentLevel(step.getNumberOfParents() + 1));
    }

    private boolean skipRenderInputOutput() {
        WebTauStep currentStep = WebTauStep.getCurrentStep();
        if (currentStep == null) {
            return false;
        }

        return verboseLevelSupplier.get() <= currentStep.getNumberOfParents() + 1;
    }

    private TokenizedMessage messageTokensForFailedStep(WebTauStep step, boolean forceFailureDisplay) {
        TokenizedMessage completionMessage = step.getCompletionMessage();
        int numberOfParents = step.getNumberOfParents();

        TokenizedMessage exceptionTokenizedMessage = step.getExceptionTokenizedMessage();

        boolean noTokenizedExceptionMessage = exceptionTokenizedMessage.isEmpty();
        if (noTokenizedExceptionMessage) {
            return completionMessage;
        }

        if (!forceFailureDisplay && step.hasFailedChildrenSteps() && !skipRenderInputOutput()) {
            // we don't render children errors one more time in case this step has failed children steps
            return completionMessage;
        }

        TokenizedMessage completionMessageWithError = tokenizedMessage().add(completionMessage);

        if (!exceptionTokenizedMessage.hasNewLineToken() && exceptionTokenizedMessage.onlyErrorTokens()) {
            return completionMessageWithError.colon().add(exceptionTokenizedMessage);
        }

        TokenizedMessage indentedAssertionMessage =
                exceptionTokenizedMessage.createReindentCopy(createIndentation(numberOfParents + 2));

        return completionMessageWithError.colon().newLine().add(indentedAssertionMessage);
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
        return completionMessage.getLastToken().getType().equals(TokenizedMessage.TokenTypes.MATCHER.getType());
    }

    private String createIndentation(int indentLevel) {
        return StringUtils.createIndentation(numberOfSpacesForIndentLevel(indentLevel));
    }

    private int numberOfSpacesForIndentLevel(int indentLevel) {
        return indentLevel * 2;
    }

    private void executeIfWithinVerboseLevel(WebTauStep step, Runnable code) {
        if (isWithinVerboseLevel(step)) {
            code.run();
        }
    }

    private static boolean isTraceStep(WebTauStep step) {
        return step.getClassifier().equals(WebTauStepClassifiers.TRACE);
    }

    private static boolean isWarningStep(WebTauStep step) {
        return step.getClassifier().equals(WebTauStepClassifiers.WARNING);
    }

    private static boolean isTraceOrWarningStep(WebTauStep step) {
        return isTraceStep(step) || isWarningStep(step);
    }
}
