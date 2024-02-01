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

package org.testingisdocumenting.webtau.expectation

import org.junit.Test
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.expectation.equality.ValuePathLazyMessageList
import org.testingisdocumenting.webtau.expectation.equality.ValuePathMessage

import java.util.stream.Stream

import static org.testingisdocumenting.webtau.WebTauCore.*

class TokenizedReportUtilsTest {
    @Test
    void "should limit number of details when specified"() {
        def messages = new ValuePathLazyMessageList()
        messages.add(new ValuePathMessage(new ValuePath("p1"), () -> tokenizedMessage().error("message 1")))
        messages.add(new ValuePathMessage(new ValuePath("p2"), () -> tokenizedMessage().error("message 2")))
        messages.add(new ValuePathMessage(new ValuePath("p3"), () -> tokenizedMessage().error("message 3")))

        def report = TokenizedReportUtils.generateReportPartWithoutLabel(new ValuePath("root"), Stream.of(messages), 2)
        actual(report.toString()).should equal("p1: message 1\n" +
                "p2: message 2\n" +
                "...")
    }

    @Test
    void "should not limit number of details when exact match in count"() {
        def messages = new ValuePathLazyMessageList()
        messages.add(new ValuePathMessage(new ValuePath("p1"), () -> tokenizedMessage().error("message 1")))
        messages.add(new ValuePathMessage(new ValuePath("p2"), () -> tokenizedMessage().error("message 2")))
        messages.add(new ValuePathMessage(new ValuePath("p3"), () -> tokenizedMessage().error("message 3")))

        def report = TokenizedReportUtils.generateReportPartWithoutLabel(new ValuePath("root"), Stream.of(messages), 3)
        actual(report.toString()).should equal("p1: message 1\n" +
                "p2: message 2\n" +
                "p3: message 3")
    }
}
