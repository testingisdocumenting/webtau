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

package org.testingisdocumenting.webtau

import org.junit.Test

import java.time.LocalDate

import static org.testingisdocumenting.webtau.Matchers.anyOf
import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.Matchers.greaterThan

class MatchersGroovyTest {
    @Test
    void "list of strings"() {
        def list = ["hello", "world"]
        list.shouldNot == ["help", "what"]
    }

    @Test
    void "string comparison example"() {
        // string-string-example
        def errorMessage = generateErrorMessage()
        errorMessage.should == "insufficient disk space" // string and string equality comparison
        // string-string-example
    }

    @Test
    void "number and string example"() {
        // string-number-example
        def numberAsText = "200"
        numberAsText.shouldBe > 150 // text and number relative comparison
        // string-number-example
    }

    @Test
    void "bean and map example"() {
        // bean-map-example
        def account = new Account("ac1", "My Account", "test account")
        account.should == [
                id: "ac1",
                name: "My Account"] // only specified properties will be compared
        // bean-map-example
    }

    @Test
    void "any of matcher example"() {
        def dateAsText = "2018-06-10"
        dateAsText.shouldBe anyOf("2018-06-11", LocalDate.of(2018, 6, 10))
    }

    @Test
    void "any of matcher with other matcher example"() {
        def dateAsText = "2018-06-10"
        dateAsText.shouldBe anyOf("2018-06-11", greaterThan(LocalDate.of(2018, 1, 1)))

        def message = "hello world"
        message.shouldNotBe anyOf("hello", contain("super"))
    }

    private static String generateErrorMessage() {
        return "insufficient disk space"
    }
}
