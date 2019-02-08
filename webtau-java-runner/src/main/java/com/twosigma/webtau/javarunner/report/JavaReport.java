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

package com.twosigma.webtau.javarunner.report;

import com.twosigma.webtau.report.Report;
import com.twosigma.webtau.report.ReportTestEntry;

/**
 * Global storage of java based report.
 * Is used to generate report at the end of all tests run.
 */
public class JavaReport {
    private static final Report report = new Report();

    public static void addTestEntry(ReportTestEntry testEntry) {
        report.addTestEntry(testEntry);
    }

    public static Report get() {
        return report;
    }
}
