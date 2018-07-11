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

package com.twosigma.webtau.report;

import com.twosigma.webtau.reporter.TestStatus;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportSummary {
    private final long total;
    private final long passed;
    private final long failed;
    private final long skipped;
    private final long errored;

    private final long startTime;
    private final long stopTime;
    private final long duration;

    ReportSummary(ReportTestEntries testEntries, long startTime, long stopTime) {
        total = testEntries.size();
        passed = testEntries.countWithStatus(TestStatus.Passed);
        failed = testEntries.countWithStatus(TestStatus.Failed);
        skipped = testEntries.countWithStatus(TestStatus.Skipped);
        errored = testEntries.countWithStatus(TestStatus.Errored);

        this.startTime = startTime;
        this.stopTime = stopTime;
        this.duration = stopTime - startTime;
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

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public long getDuration() {
        return duration;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("skipped", skipped);
        result.put("errored", errored);
        result.put("startTime", startTime);
        result.put("stopTime", stopTime);
        result.put("duration", duration);

        return result;
    }
}
