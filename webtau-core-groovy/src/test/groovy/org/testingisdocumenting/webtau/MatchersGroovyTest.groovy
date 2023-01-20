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

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.anyOf
import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.Matchers.greaterThan

class MatchersGroovyTest {
    private final List<String> messages = Arrays.asList("message one", "message two", "message we wait for")
    private int messagesIdx = 0

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
    void "string wait example"() {
        // wait-consume-message
        actual(this.&consumeMessage).waitTo == "message we wait for"
        // wait-consume-message
    }

    private String consumeMessage() {
        return messages.get(messagesIdx++)
    }

    @Test
    void "number and string example"() {
        // string-number-example
        def numberAsText = "200"
        numberAsText.shouldBe > 150 // text and number relative comparison
        // string-number-example
    }

    @Test
    void "number comparison example"() {
        // number-number-example
        double price = calculatePrice()
        actual(price, "price").shouldBe > 10 // explict name to use in reporting
        // number-number-example
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
        dateAsText.should == anyOf("2018-06-11", LocalDate.of(2018, 6, 10))
    }

    @Test
    void "any of matcher with other matcher example"() {
        def dateAsText = "2018-06-10"
        dateAsText.should == anyOf("2018-06-11", greaterThan(LocalDate.of(2018, 1, 1)))

        def message = "hello world"
        message.shouldNot == anyOf("hello", contain("super"))
    }

    private static String generateErrorMessage() {
        return "insufficient disk space"
    }

    private static double calculatePrice() {
        return 10.5
    }
}
