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

package org.testingisdocumenting.webtau.runner.standalone

import org.testingisdocumenting.webtau.reporter.TestResultPayload
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauCore.equal

class StandaloneTestTest {
    @Test
    void "registered payloads should be merged into toMap test representation"() {
        def test = new StandaloneTest(Paths.get("").toAbsolutePath(),
                Paths.get("test.groovy").toAbsolutePath(), "test.groovy", "my test description", {})
        test.test.startTime = 12345678
        test.test.elapsedTime = 100
        test.addResultPayload(new TestResultPayload("screenshot", "base64"))
        test.addResultPayload(new TestResultPayload("steps", ["step1", "step2"]))

        test.getTest().toMap().should equal([id              : 'test.groovy-1',
                                             scenario        : 'my test description',
                                             fileName        : 'test.groovy',
                                             shortContainerId: 'test.groovy',
                                             startTime       : 12345678,
                                             elapsedTime     : 100,
                                             status          : 'Skipped',
                                             disabled        : false,
                                             metadata        : [:],
                                             screenshot      : 'base64', steps: ['step1', 'step2']])
    }
}
