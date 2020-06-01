/*
 * Copyright 2020 webtau maintainers
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

package listeners

import org.testingisdocumenting.webtau.reporter.TestListener

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.action

class BeforeAfterAllTestsListener implements TestListener {
    @Override
    void beforeFirstTest() {
        action("action before first test") {
            println "some action"
        }()
    }

    @Override
    void afterAllTests() {
        action("action after all tests") {
            println "some action"
        }()
    }
}
