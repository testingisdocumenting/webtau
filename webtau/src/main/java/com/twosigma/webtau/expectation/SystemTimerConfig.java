/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.expectation.timer.ExpectationTimerConfig;
import com.twosigma.webtau.cfg.WebTauConfig;

public class SystemTimerConfig implements ExpectationTimerConfig {
    @Override
    public ExpectationTimer createExpectationTimer() {
        return new SystemTimeExpectationTimer();
    }

    @Override
    public long defaultTimeoutMillis() {
        return getCfg().waitTimeout();
    }

    @Override
    public long defaultTickMillis() {
        return 100;
    }

    private WebTauConfig getCfg() {
        return WebTauConfig.getInstance();
    }
}
