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

scenario('wrap as step') {
    // wrap-step
    step("important actions") {
        actionOne()
        actionTwo()
    }
    // wrap-step
}

scenario('wrap as step with input') {
    def myPort = 8080
    def baseUrl = "http://baseurl"
    // wrap-step-key-value
    step("group of actions", [url: baseUrl, port: myPort]) {
        actionThree(myPort, baseUrl)
    }
    // wrap-step-key-value
}

def actionOne() {
    step("action one") {
        // ...
    }
}

def actionTwo() {
    step("action two") {
        // ...
    }
}

def actionThree(port, url) {
    step("action three") {
        // ...
    }
}