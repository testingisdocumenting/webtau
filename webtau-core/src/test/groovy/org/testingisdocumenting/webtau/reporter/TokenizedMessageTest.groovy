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

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*

class TokenizedMessageTest {
    @Test
    void "render to string"() {
        def message = tokenizedMessage().action("hello").colon().string("world")
        actual(message.toString()).should(equal("hello: world"))
    }

    @Test
    void "value with null"() {
        def message = tokenizedMessage().matcher("equals to").value(null)
        actual(message.toString()).should(equal("equals to null"))
    }

    @Test
    void "value with string"() {
        def message = tokenizedMessage().matcher("equals to").value("hello\nworld")
        actual(message.toString()).should(equal('equals to _____\n' +
                '          hello\n' +
                '          world\n' +
                '          _____'))
    }
}
