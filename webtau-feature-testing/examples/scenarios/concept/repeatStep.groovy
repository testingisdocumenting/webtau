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

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('multiple times') {
    repeatStep('my actions', 30) {
        step("custom step one") {
            step("nested step one") {
            }
        }

        step("custom step two") {
        }
    }
}

scenario('multiple times with context') {
    def result = []

    repeatStep("step with context", 5) { ctx ->
        println ctx.attemptNumber
        result << ctx.attemptNumber
    }

    result.should == [1, 2, 3, 4, 5]
}