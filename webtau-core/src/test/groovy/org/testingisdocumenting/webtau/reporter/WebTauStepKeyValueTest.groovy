/*
 * Copyright 2023 webtau maintainers
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
import org.testingisdocumenting.webtau.data.render.PrettyPrintableTestBase

import static org.testingisdocumenting.webtau.WebTauCore.*

class WebTauStepKeyValueTest extends PrettyPrintableTestBase {
    @Test
    void "print simple key values"() {
        prettyPrint([key1: 100, key2: "value"], 'key1: 100\n' +
                'key2: "value"')
    }

    @Test
    void "pretty print map and table as values"() {
        def table = table("columnA", "columnB",
                          _____________________,
                            100    , "hello",
                            200    , "world")

        prettyPrint([key1: [nested: "value", another: 100], key2: table], 'key1: {"nested": "value", "another": 100}\n' +
                'key2: columnA │ columnB\n' +
                '          100 │ "hello"\n' +
                '          200 │ "world"')
    }

    void prettyPrint(Map<String, ?> keyValue, String expected) {
        WebTauStepKeyValue.prettyPrint(printer, keyValue)
        expectOutput(expected)
    }
}
