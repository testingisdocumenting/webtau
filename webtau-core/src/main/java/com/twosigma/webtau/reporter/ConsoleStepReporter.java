/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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
