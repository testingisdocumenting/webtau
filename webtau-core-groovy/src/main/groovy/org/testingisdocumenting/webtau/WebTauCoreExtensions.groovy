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

import org.testingisdocumenting.webtau.reporter.WebTauStepContext

import java.util.function.Function
import java.util.function.Supplier

class WebTauCoreExtensions {
    /**
     * override groovy default extensions to call webtau version
     * without extension, even though WebTauCore static import is present, sleep is still being called from default methods
     *
     * @param o ignored object
     * @param millis millis to sleep
     */
    static void sleep(Object o, long millis) {
        WebTauCore.sleep(millis)
    }

    static <R> R step(Object o, String label, Closure action) {
        return WebTauCore.step(label, action)
    }

    static <R> R step(Object o, String label, Map<String, Object> inputs, Closure action) {
        return WebTauCore.step(label, inputs, action as Supplier)
    }

    static void repeatStep(Object o, String label, int numberOfAttempts, Closure action) {
        WebTauCore.repeatStep(label, numberOfAttempts, (ctx) -> {
            return action(ctx)
        } as Function<WebTauStepContext, Object>)
    }
}
