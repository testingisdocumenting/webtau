package com.twosigma.webtau.reporter;

import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.utils.StringUtils;

public class ConsoleStepReporter implements StepReporter {
    private TokenizedMessageToAnsiConverter toAnsiConverter;

    public ConsoleStepReporter(TokenizedMessageToAnsiConverter toAnsiConverter) {
        this.toAnsiConverter = toAnsiConverter;
    }

    @Override
    public void onStepStart(TestStep step) {
        ConsoleOutputs.out(indentationStep(step), Color.YELLOW, "> ", toAnsiConverter.convert(step.getInProgressMessage()));
    }

    @Override
    public void onStepSuccess(TestStep step) {
        ConsoleOutputs.out(indentationStep(step), Color.GREEN, ". ", toAnsiConverter.convert(step.getCompletionMessage()));
    }

    @Override
    public void onStepFailure(TestStep step) {
        ConsoleOutputs.out(indentationStep(step),Color.RED, "X ", toAnsiConverter.convert(step.getCompletionMessage()));
    }

    private String indentationStep(TestStep step) {
        return StringUtils.createIndentation(step.getNumberOfParents() * 2);
    }
}
