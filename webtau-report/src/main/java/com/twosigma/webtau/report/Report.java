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

import java.util.List;
import java.util.stream.Collectors;

public class Report {
    private long startTime;
    private long stopTime;
    private ReportTestEntries testEntries;

    public Report() {
        this.testEntries = new ReportTestEntries();
    }

    public Report(List<ReportTestEntry> testEntries, long startTime, long stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.testEntries = new ReportTestEntries(testEntries);
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        stopTime = System.currentTimeMillis();
    }

    public void addTestEntry(ReportTestEntry entry) {
        testEntries.add(entry);
    }

    public ReportSummary createSummary() {
        return new ReportSummary(testEntries, startTime, stopTime);
    }

    public ReportTestEntries getTestEntries() {
        return testEntries;
    }

    public List<ReportCustomData> extractReportCustomData() {
        return ReportDataProviders.provide(this.testEntries)
                .collect(Collectors.toList());
    }
}
