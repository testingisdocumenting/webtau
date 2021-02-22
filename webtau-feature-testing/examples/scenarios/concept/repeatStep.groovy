/*
 * Copyright 2021 webtau maintainers
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

package scenarios.concept

import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder
import org.testingisdocumenting.webtau.reporter.StepReportOptions
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.TokenizedMessage
import org.testingisdocumenting.webtau.reporter.WebTauStep

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('multiple times') {
    repeat(5) {
        step("custom step one") {
            step("nested step one") {
                // http.put("for example")
            }
        }

        step("custom step two") {
        }
    }
}

static def repeat(int number, Closure code) {
    def repeatStep = WebTauStep.createStep(null, 0,
            TokenizedMessage.tokenizedMessage(IntegrationTestsMessageBuilder.action("do repeat")),
            () -> TokenizedMessage.tokenizedMessage(IntegrationTestsMessageBuilder.action("do repeat")),
            () -> code())

    repeatStep.setTotalNumberOfAttempts(number)
    repeatStep.execute(StepReportOptions.REPORT_ALL)
}