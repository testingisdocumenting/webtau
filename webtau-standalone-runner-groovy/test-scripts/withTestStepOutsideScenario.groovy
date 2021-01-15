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

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createAndExecuteStep
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage

createAndExecuteStep(tokenizedMessage(action("running errand")),
        { -> tokenizedMessage(action("ran errand"))}) {
}

scenario('scenario one') {
}

scenario('scenario two') {
}

scenario('scenario three') {
}
