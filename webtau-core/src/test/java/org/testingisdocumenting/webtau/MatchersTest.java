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

import org.junit.*;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.reporter.StepReporters;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class MatchersTest {
    private final List<String> messages = Arrays.asList("message one", "message two", "message we wait for");
    private int messagesIdx = 0;
    private int numberOfRecords = 0;

    @Before
    public void addStepReporter() {
        StepReporters.add(StepReporters.defaultStepReporter);
    }

    @After
    public void removeStepReporter() {
        StepReporters.remove(StepReporters.defaultStepReporter);
    }

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
    public void beanAndMapEqualityExample() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "bean-map-compare-output", "X failed expecting [value] to equal {\"id\": \"ac1\", \"name\": \"My Second Account\", \"address\": {\"zipCode\": \"7777777\"}}:\n" +
                "    [value].name:  actual: \"My Account\" <java.lang.String>\n" +
                "                 expected: \"My Second Account\" <java.lang.String>\n" +
                "                               ^\n" +
                "    [value].address.zipCode:  actual: \"88888888\" <java.lang.String>\n" +
                "                            expected: \"7777777\" <java.lang.String>\n" +
                "                                       ^ (Xms)\n" +
                "  \n" +
                "  {\n" +
                "    \"address\": {\"city\": \"TestingCity\", \"zipCode\": **\"88888888\"**},\n" +
                "    \"description\": \"test account\",\n" +
                "    \"id\": \"ac1\",\n" +
                "    \"name\": **\"My Account\"**\n" +
                "  }", () -> {
            // bean-map-example
            Address address = new Address("TestingCity", "88888888");
            Account account = new Account("ac1", "My Account", "test account", address);
            actual(account).should(equal(map( // utility function from WebTauCore static import
                    "id", "ac1",
                    "name", "My Second Account",
                    "address", map("zipCode", "7777777")))); // only specified properties will be compared
            // bean-map-example
        });
    }

    @Test
    public void beanAndMapContainsExample() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "list-of-beans-map-contain", "X failed expecting [value] to not contain {\"id\": \"ac2\", \"name\": \"Work\"}:\n" +
                "    [value][1].id:  actual:     \"ac2\" <java.lang.String>\n" +
                "                  expected: not \"ac2\" <java.lang.String>\n" +
                "    [value][1].name:  actual:     \"Work\" <java.lang.String>\n" +
                "                    expected: not \"Work\" <java.lang.String> (Xms)\n" +
                "  \n" +
                "  [\n" +
                "    {\n" +
                "      \"address\": org.testingisdocumenting.webtau.Address@<ref>,\n" +
                "      \"description\": \"test account\",\n" +
                "      \"id\": \"ac1\",\n" +
                "      \"name\": \"Home\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"address\": org.testingisdocumenting.webtau.Address@<ref>,\n" +
                "      \"description\": \"test account\",\n" +
                "      \"id\": **\"ac2\"**,\n" +
                "      \"name\": **\"Work\"**\n" +
                "    },\n" +
                "    {\n" +
                "      \"address\": org.testingisdocumenting.webtau.Address@<ref>,\n" +
                "      \"description\": \"test account\",\n" +
                "      \"id\": \"ac3\",\n" +
                "      \"name\": \"My Account\"\n" +
                "    }\n" +
                "  ]", () -> {
            // bean-map-contains-example
            List<Account> accounts = fetchAccounts();
            actual(accounts).shouldNot(contain(map("id", "ac2", "name", "Work"))); // only check specified properties
            // bean-map-contains-example
        });
    }

    @Test
    public void listOfBeansAndTableEqualityExample() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "beans-table-compare-output", "X failed expecting [value] to equal *id   │ name         │ address            \n" +
                "                                    \"ac2\" │ \"Works\"      │ {\"zipCode\": \"zip2\"}\n" +
                "                                    \"ac1\" │ \"Home\"       │ {\"zipCode\": \"zip1\"}\n" +
                "                                    \"ac3\" │ \"My Account\" │ {\"zipCode\": \"zip8\"}:\n" +
                "    [value][2].address.zipCode:  actual: \"zip3\" <java.lang.String>\n" +
                "                               expected: \"zip8\" <java.lang.String>\n" +
                "                                             ^\n" +
                "    [value][1].name:  actual: \"Work\" <java.lang.String>\n" +
                "                    expected: \"Works\" <java.lang.String>\n" +
                "                                   ^ (Xms)\n" +
                "  \n" +
                "  address                                │ description    │ id    │ name        \n" +
                "  {\"city\": \"TC1\", \"zipCode\": \"zip1\"}     │ \"test account\" │ \"ac1\" │ \"Home\"      \n" +
                "  {\"city\": \"TC2\", \"zipCode\": \"zip2\"}     │ \"test account\" │ \"ac2\" │ **\"Work\"**  \n" +
                "  {\"city\": \"TC3\", \"zipCode\": **\"zip3\"**} │ \"test account\" │ \"ac3\" │ \"My Account\"", () -> {
            // beans-table-example
            List<Account> accounts = fetchAccounts();
            TableData expected = table("*id",       "name", "address",  // id is a key column
                                       ________________________________________,
                                       "ac2",      "Works", map("zipCode", "zip2"), // when key is present, comparison is order agnostic
                                       "ac1",       "Home", map("zipCode", "zip1"),
                                       "ac3", "My Account", map("zipCode", "zip8"));

            actual(accounts).should(equal(expected));
            // beans-table-example
        });
    }

    @Test
    public void listOfBeansAndTableContainExample() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "beans-table-contain-output", "X failed expecting [value] to contain id    │ name    │ address            \n" +
                "                                      \"ac2\" │ \"Works\" │ {\"zipCode\": \"zip2\"}\n" +
                "                                      \"ac1\" │ \"Home\"  │ {\"zipCode\": \"zip1\"}:\n" +
                "    no matches found for: [{\"id\": \"ac2\", \"name\": \"Works\", \"address\": {\"zipCode\": \"zip2\"}}] (Xms)\n" +
                "  \n" +
                "  [\n" +
                "    {\n" +
                "      \"address\": {\"city\": \"TC1\", \"zipCode\": \"zip1\"},\n" +
                "      \"description\": \"test account\",\n" +
                "      \"id\": \"ac1\",\n" +
                "      \"name\": \"Home\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"address\": {\"city\": \"TC2\", \"zipCode\": \"zip2\"},\n" +
                "      \"description\": \"test account\",\n" +
                "      \"id\": \"ac2\",\n" +
                "      \"name\": \"Work\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"address\": {\"city\": \"TC3\", \"zipCode\": \"zip3\"},\n" +
                "      \"description\": \"test account\",\n" +
                "      \"id\": \"ac3\",\n" +
                "      \"name\": \"My Account\"\n" +
                "    }\n" +
                "  ]", () -> {
            // beans-table-contain-example
            List<Account> accounts = fetchAccounts();
            TableData expected = table("id",  "name", "address",
                                       _______________________________________,
                                       "ac2", "Works", map("zipCode", "zip2"),
                                       "ac1",  "Home", map("zipCode", "zip1"));

            actual(accounts).should(contain(expected));
            // beans-table-contain-example
        });
    }

    private static List<Account> fetchAccounts() {
        return Arrays.asList(
                new Account("ac1", "Home", "test account", new Address("TC1", "zip1")),
                new Account("ac2", "Work", "test account", new Address("TC2", "zip2")),
                new Account("ac3", "My Account", "test account", new Address("TC3", "zip3")));
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
