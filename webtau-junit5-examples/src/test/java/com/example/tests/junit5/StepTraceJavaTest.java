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

package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.data.Account;
import org.testingisdocumenting.webtau.junit5.WebTau;

import java.util.Arrays;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;

@WebTau
public class StepTraceJavaTest {
    @Test
    public void wrapAsStep() {
        // wrap-step
        step("group of actions", () -> {
            actionOne();
            actionTwo();
        });
        // wrap-step
    }

    @Test
    public void wrapAsStepWithKeyValue() {
        int myPort = 8080;
        String baseUrl = "http://baseurl";

        // wrap-step-key-value
        step("important actions", map("myPort", myPort, "baseUrl", baseUrl), () -> {
            actionThree(myPort, baseUrl);
        });
        // wrap-step-key-value
    }

    @Test
    public void tracingKeyValues() {
        // trace-map
        trace("trace label", map("k1", "v1", "k2", "v2"));
        // trace-map
        // trace-vararg
        trace("another trace label", "k3", "v3", "k4", "v4");
        // trace-vararg
    }

    @Test
    public void tracingJavaBeans() {
        List<Account> accounts = fetchAccounts();
        trace("my accounts", propertiesTable(accounts));
    }

    public static void actionOne() {
    }

    public static void actionTwo() {
    }

    public static void actionThree(int port, String url) {
        trace("action three");
    }

    private static List<Account> fetchAccounts() {
        return Arrays.asList(
                new Account("a1", "Account One", 100),
                new Account("a2", "Account Two", 130),
                new Account("a3", "Account Three", 70));
    }
}
