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

package com.twosigma.webtau.runner.standalone

import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.Ddjt.equal

class StandaloneTestTest {
    @Test
    void "registered payloads should be merged into toMap test representation"() {
        def test = new StandaloneTest(Paths.get("."), Paths.get("test.groovy"), "my test description", {})
        test.addResultPayload({ return [screenshot: "base64" ]})
        test.addResultPayload({ return [steps: ["step1", "step2"] ]})

        test.toMap().should equal([id: 'test.groovy-1',
                                   scenario: 'my test description',
                                   fileName: 'test.groovy',
                                   assertion: null,
                                   contextDescription: null,
                                   shortStackTrace: null,
                                   fullStackTrace: null,
                                   exceptionMessage: null,
                                   failedCodeSnippets: null,
                                   status: 'Skipped',
                                   screenshot: 'base64', steps: ['step1', 'step2']])
    }
}
