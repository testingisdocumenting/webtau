/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation.timer;

import com.twosigma.webtau.utils.ServiceUtils;

import java.util.List;

public class ExpectationTimerConfigProvider {
    private static List<ExpectationTimerConfig> configs = ServiceUtils.discover(ExpectationTimerConfig.class);

    public static ExpectationTimer createExpectationTimer() {
        validate();
        return configs.get(0).createExpectationTimer();
    }

    public static long defaultTickMillis() {
        validate();
        return configs.get(0).defaultTickMillis();
    }

    public static long defaultTimeoutMillis() {
        validate();
        return configs.get(0).defaultTimeoutMillis();
    }

    private static void validate() {
        if (configs.isEmpty()) {
            throw new RuntimeException("no " + ExpectationTimerConfig.class + " registered");
        }

        if (configs.size() > 1) {
            throw new RuntimeException("more than one " + ExpectationTimerConfig.class + " is registered");
        }
    }
}
