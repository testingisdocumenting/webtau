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

package org.testingisdocumenting.webtau.notebook

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*

class PrettyPrintableToHtmlConverterTest {
    @Test
    void "convert table"() {
        def myTable = table("colA", "colB",
                            _______________,
                            100, "world")

        def result = PrettyPrintableToHtmlConverter.convert(myTable)

        actual(result).should(equal("<pre>" +
                "<span style=\"\"></span><span style=\"color: var(--md-purple-300)\">colA</span><span style=\"color: var(--md-yellow-700)\"> │ </span><span style=\"color: var(--md-purple-300)\">colB   </span>\n" +
                "<span style=\"\"> </span><span style=\"color: var(--md-blue-500)\">100</span><span style=\"color: var(--md-yellow-700)\"> │ </span><span style=\"color: var(--md-green-500)\">\"world\"</span>" +
                "</pre>"))
    }
}
