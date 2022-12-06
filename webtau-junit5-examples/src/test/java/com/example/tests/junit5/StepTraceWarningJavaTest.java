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
import org.testingisdocumenting.webtau.junit5.WebTau;
import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class StepTraceWarningJavaTest {
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
        step("group of actions", aMapOf("myPort", myPort, "baseUrl", baseUrl), () -> {
            actionThree(myPort, baseUrl);
        });
    }

    public static void actionOne() {
        trace("action one");
    }

    public static void actionTwo() {

    }

    public static void actionThree(int port, String url) {
        trace("action three");
    }
}
