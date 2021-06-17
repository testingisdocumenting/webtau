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

package org.testingisdocumenting.webtau.reporter;

public class WebTauStepContext {
    public static final WebTauStepContext SINGLE_RUN = new WebTauStepContext(0, 1);

    private final int attemptIdx;
    private final int totalNumberOfAttempts;

    public WebTauStepContext(int attemptIdx, int totalNumberOfAttempts) {
        this.attemptIdx = attemptIdx;
        this.totalNumberOfAttempts = totalNumberOfAttempts;
    }

    public int getAttemptIdx() {
        return attemptIdx;
    }

    public int getAttemptNumber() {
        return attemptIdx + 1;
    }

    public int getTotalNumberOfAttempts() {
        return totalNumberOfAttempts;
    }
}
