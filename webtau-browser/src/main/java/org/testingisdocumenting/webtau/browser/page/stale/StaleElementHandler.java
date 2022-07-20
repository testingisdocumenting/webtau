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

package org.testingisdocumenting.webtau.browser.page.stale;

import org.openqa.selenium.StaleElementReferenceException;
import org.testingisdocumenting.webtau.browser.BrowserConfig;

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class StaleElementHandler {
    private StaleElementHandler() {
    }

    public static <R> R getValueForStaleElement(Supplier<R> code, R valueInCaseOfStale) {
        try {
            return code.get();
        } catch (StaleElementReferenceException e) {
            return valueInCaseOfStale;
        }
    }

    public static Object repeatForStaleElement(Supplier<Object> code) {
        int numberOfAttemptsLeft = BrowserConfig.getStaleElementRetry();

        for (; numberOfAttemptsLeft >= 1; numberOfAttemptsLeft--) {
            try {
                return code.get();
            } catch (StaleElementReferenceException e) {
                if (numberOfAttemptsLeft == 1) {
                    throw new RuntimeException("element is stale, " +
                            "consider using waitToBe visible matcher to make sure component fully appeared");
                }

                sleep(BrowserConfig.getStaleElementRetryWait());
            }
        }

        throw new IllegalStateException("shouldn't reach this point");
    }


}
