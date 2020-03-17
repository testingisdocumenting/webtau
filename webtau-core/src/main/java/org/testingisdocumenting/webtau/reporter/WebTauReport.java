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

package org.testingisdocumenting.webtau.reporter;

public class WebTauReport {
    private final long startTime;
    private final long stopTime;
    private final WebTauTestList tests;

    private final long total;
    private final long passed;
    private final long failed;
    private final long skipped;
    private final long errored;
    private final long duration;

    public WebTauReport(WebTauTestList tests, long startTime, long stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.tests = tests;

        duration = stopTime - startTime;

        total = tests.size();
        passed = tests.countWithStatus(TestStatus.Passed);
        failed = tests.countWithStatus(TestStatus.Failed);
        skipped = tests.countWithStatus(TestStatus.Skipped);
        errored = tests.countWithStatus(TestStatus.Errored);
    }

    public WebTauTestList getTests() {
        return tests;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public long getDuration() {
        return duration;
    }

    public long getTotal() {
        return total;
    }

    public long getPassed() {
        return passed;
    }

    public long getFailed() {
        return failed;
    }

    public long getSkipped() {
        return skipped;
    }

    public long getErrored() {
        return errored;
    }
}
