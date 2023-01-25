/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau;

import org.junit.Test;
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

// import-dsl
import static org.testingisdocumenting.webtau.WebTauCore.*;
// import-dsl

public class MatchersTest {
    private final List<String> messages = Arrays.asList("message one", "message two", "message we wait for");
    private int messagesIdx = 0;
    private int numberOfRecords = 0;

    @Test
    public void stringComparisonExample() {
        doc.console.capture("string-string-comparison", () -> {
            // string-string-example
            String errorMessage = generateErrorMessage();
            actual(errorMessage).should(equal("insufficient disk space")); // string and string equality comparison
            // string-string-example
        });
    }

    @Test
    public void stringWaitExample() {
        doc.console.capture("wait-message", () -> {
            // wait-consume-message
            actual(liveValue(this::consumeMessage)).waitTo(equal("message we wait for"));
            // wait-consume-message
        });
    }

    @Test
    public void numberWaitExample() {
        // wait-number-records
        actual(liveValue(this::countRecords)).waitToBe(greaterThanOrEqual(5));
        // wait-number-records
    }

    private String consumeMessage() {
        return messages.get(messagesIdx++);
    }

    private int countRecords() {
        numberOfRecords += 1;
        return numberOfRecords;
    }

    @Test
    public void numberComparisonExample() {
        doc.console.capture("number-number-comparison", () -> {
            // number-number-example
            double price = calculatePrice();
            actual(price, "price").shouldBe(greaterThan(10)); // explict name to use in reporting
            // number-number-example
        });
    }

    @Test
    public void numberAndStringExample() {
        // string-number-example
        String numberAsText = "200";
        actual(numberAsText).shouldBe(greaterThan(150)); // text and number relative comparison
        // string-number-example
    }

    @Test
    public void beanAndMapExample() {
        TestConsoleOutput.runCaptureAndValidateOutput("bean-map-compare-output", "X failed expecting [value] to equal {id=ac1, name=My Second Account}: \n" +
                        "    mismatches:\n" +
                        "    \n" +
                        "    [value].name:   actual: \"My Account\" <java.lang.String>\n" +
                        "                  expected: \"My Second Account\" <java.lang.String>\n" +
                        "                                ^ (Xms)\n" +
                        "  {\n" +
                        "    \"description\": \"test account\",\n" +
                        "    \"id\": \"ac1\",\n" +
                        "    \"name\": **\"My Account\"**\n" +
                        "  }", () -> {
            // bean-map-example
            Account account = new Account("ac1", "My Account", "test account");
            actual(account).should(equal(map( // utility function from WebTauCore static import
                    "id", "ac1",
                    "name", "My Second Account"))); // only specified properties will be compared
            // bean-map-example
        });
    }

    @Test
    public void anyOfMatcherExample() {
        String dateAsText = "2018-06-10";
        actual(dateAsText).shouldBe(anyOf("2018-06-11", LocalDate.of(2018, 6, 10)));
    }

    @Test
    public void anyOfMatcherWithOtherMatcherExample() {
        String dateAsText = "2018-06-10";
        actual(dateAsText).shouldBe(anyOf("2018-06-11", greaterThan(LocalDate.of(2018, 1, 1))));

        String message = "hello world";
        actual(message).shouldNotBe(anyOf("hello", contain("super")));
    }

    @Test
    public void listFailureExample() {
        doc.console.capture("list-failure", () -> {
            code(() -> {
                List<?> values = Arrays.asList(
                        1,
                        "testing",
                        map("key1", "hello", "key2", "world"));
                // failed-list
                actual(values).should(equal(Arrays.asList(
                        1,
                        "teasing",
                        map("key1", "hello", "key2", "work"))));
                // failed-list
            }).should(throwException(AssertionError.class));
        });
    }


    private static String generateErrorMessage() {
        return "insufficient disk space";
    }

    private static double calculatePrice() {
        return 10.5;
    }
}
