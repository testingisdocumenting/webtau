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

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.COLON
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.stringValue

class TokenizedMessageTest {
    @Test
    void "render to string"() {
        def message = TokenizedMessage.tokenizedMessage(action("hello"), COLON, stringValue("world"))
        actual(message.toString()).should(equal("hello: world"))
    }
}
