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

    private static String generateErrorMessage() {
        return "insufficient disk space"
    }
}
