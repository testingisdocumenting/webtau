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

package org.testingisdocumenting.webtau.expectation;

import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepClassifiers;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

public class ActualCode implements ActualCodeExpectations {
    private final CodeBlock actual;

    public ActualCode(CodeBlock actual) {
        this.actual = actual;
    }

    @Override
    public void should(CodeMatcher codeMatcher) {
        executeStep(codeMatcher,
                tokenizedMessage().action("expecting"),
                () -> shouldStep(codeMatcher), StepReportOptions.REPORT_ALL);
    }

    private void shouldStep(CodeMatcher codeMatcher) {
        boolean matches = codeMatcher.matches(actual);

        if (matches) {
            handleMatch(codeMatcher);
        } else {
            handleMismatch(codeMatcher, codeMatcher.mismatchedTokenizedMessage(actual));
        }
    }

    private void handleMatch(CodeMatcher codeMatcher) {
        ExpectationHandlers.onCodeMatch(codeMatcher);
    }

    private void handleMismatch(CodeMatcher codeMatcher, TokenizedMessage message) {
        final ExpectationHandler.Flow flow = ExpectationHandlers.onCodeMismatch(codeMatcher, message);

        if (flow != ExpectationHandler.Flow.Terminate) {
            throw new AssertionTokenizedError(message);
        }
    }

    private void executeStep(CodeMatcher codeMatcher,
                             TokenizedMessage messageStart,
                             Runnable expectationValidation,
                             StepReportOptions stepReportOptions) {
        TokenizedMessage codeDescription = tokenizedMessage().id("code");
        WebTauStep step = createStep(
                messageStart.add(codeDescription).add(codeMatcher.matchingTokenizedMessage()),
                () -> tokenizedMessage(codeDescription)
                        .add(codeMatcher.matchedTokenizedMessage(actual)),
                expectationValidation);
        step.setClassifier(WebTauStepClassifiers.MATCHER);

        step.execute(stepReportOptions);
    }
}
