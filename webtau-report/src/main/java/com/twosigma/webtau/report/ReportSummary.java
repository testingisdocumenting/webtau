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
    private long total;
    private long passed;
    private long failed;
    private long skipped;
    private long errored;

    public ReportSummary(ReportTestEntries testEntries) {
        total = testEntries.size();
        passed = testEntries.countWithStatus(TestStatus.Passed);
        failed = testEntries.countWithStatus(TestStatus.Failed);
        skipped = testEntries.countWithStatus(TestStatus.Skipped);
        errored = testEntries.countWithStatus(TestStatus.Errored);
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

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("skipped", skipped);
        result.put("errored", errored);

        return result;
    }
}
