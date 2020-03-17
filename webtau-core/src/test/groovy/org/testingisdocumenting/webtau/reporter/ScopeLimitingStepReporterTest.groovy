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

package org.testingisdocumenting.webtau.reporter

import org.junit.Test

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action
import static org.junit.Assert.assertEquals

class ScopeLimitingStepReporterTest implements StepReporter {
    private List<String> capturedStepsDesc

    @Test
    void "should not delegate to step reporter if step nest level is greater than provided max"() {
        expectReport(0, '') {
            executeSteps()
        }

        expectReport(1, '> top level action\n' +
                'X failed top level action : out of memory') {
            executeSteps()
        }

        expectReport(2, '> top level action\n' +
                '> second level action\n' +
                '> second level action completed\n' +
                'X failed top level action : out of memory') {
            executeSteps()
        }

        expectReport(3, '> top level action\n' +
                '> second level action\n' +
                '> second level action completed\n' +
                '> second level action with error\n' +
                '> third level action\n' +
                'X failed third level action : out of memory\n' +
                'X failed second level action with error : out of memory\n' +
                'X failed top level action : out of memory') {
            executeSteps()
        }
    }

    private static void executeSteps() {
        def topLevelStep = TestStep.createStep(null, TokenizedMessage.tokenizedMessage(action("top level action")),
                { -> TokenizedMessage.tokenizedMessage(action("top level action completed")) }) {

            def secondLevelStepSuccess = TestStep.createStep(null, TokenizedMessage.tokenizedMessage(action("second level action")),
                    { -> TokenizedMessage.tokenizedMessage(action("second level action completed")) }) {
            }

            def secondLevelStepFailure = TestStep.createStep(null, TokenizedMessage.tokenizedMessage(action("second level action with error")),
                    { -> TokenizedMessage.tokenizedMessage(action("second level action with error completed")) }) {

                def thirdLevelStep = TestStep.createStep(null, TokenizedMessage.tokenizedMessage(action("third level action")),
                        { -> TokenizedMessage.tokenizedMessage(action("third level action completed")) }) {
                    throw new RuntimeException('out of memory')
                }

                thirdLevelStep.execute(StepReportOptions.REPORT_ALL)
            }

            secondLevelStepSuccess.execute(StepReportOptions.REPORT_ALL)
            secondLevelStepFailure.execute(StepReportOptions.REPORT_ALL)
        }

        topLevelStep.execute(StepReportOptions.REPORT_ALL)
    }

    private void expectReport(int maxLevel, String expectedReport, Closure code) {
        def stepReporter = new ScopeLimitingStepReporter(this, maxLevel)

        capturedStepsDesc = []
        try {
            StepReporters.add(stepReporter)
            code.run()
        } catch(ignored) {
        }

        StepReporters.remove(stepReporter)
        assertEquals(expectedReport, capturedStepsDesc.join('\n'))
    }

    @Override
    void onStepStart(TestStep step) {
        capturedStepsDesc.add('> ' + step.inProgressMessage.toString())
    }

    @Override
    void onStepSuccess(TestStep step) {
        capturedStepsDesc.add('> ' + step.completionMessage.toString())
    }

    @Override
    void onStepFailure(TestStep step) {
        capturedStepsDesc.add('X ' + step.completionMessage.toString())
    }
}
