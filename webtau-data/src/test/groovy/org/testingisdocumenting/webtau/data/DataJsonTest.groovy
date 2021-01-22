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

package org.testingisdocumenting.webtau.data

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep
import org.testingisdocumenting.webtau.utils.JsonParseException

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.Matchers.throwException

class DataJsonTest implements StepReporter {
    def failedSteps = []

    @Before
    void init() {
        StepReporters.add(this)
        failedSteps.clear()
    }

    @After
    void cleanup() {
        StepReporters.remove(this)
    }

    @Test
    void "parse json as map"() {
        Map map = new DataJson().map("map.json")
        map.should == [key: 'value', another: 2]
    }

    @Test
    void "parse json as list"() {
        List list = new DataJson().list("list.json")
        list.should == [
                [key: 'value1', another: 1],
                [key: 'value2', another: 2]]
    }

    @Test
    void "parse json as object"() {
        def object = new DataJson().object("map.json")
        object.should == [key: 'value', another: 2]
    }

    @Test
    void "parse json error should capture as failed step"() {
        code {
            def object = new DataJson().object("broken.json")
            object.should == [key: 'value', another: 2]
        } should throwException(JsonParseException)

        failedSteps[0].completionMessage.toString().should contain("failed reading json from file or resource broken.json : Unexpected character")
    }

    @Override
    void onStepStart(WebTauStep step) {

    }

    @Override
    void onStepSuccess(WebTauStep step) {

    }

    @Override
    void onStepFailure(WebTauStep step) {
        failedSteps.add(step)
    }
}
