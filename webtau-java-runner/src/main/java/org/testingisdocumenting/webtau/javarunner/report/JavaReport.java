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

package org.testingisdocumenting.webtau.javarunner.report;

import org.testingisdocumenting.webtau.reporter.WebTauReport;
import org.testingisdocumenting.webtau.reporter.WebTauReportName;
import org.testingisdocumenting.webtau.reporter.WebTauTest;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;
import org.testingisdocumenting.webtau.time.Time;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*;

/**
 * Global storage of java based report.
 * Is used to generate report at the end of all tests run.
 */
public class JavaReport {
    public static final JavaReport INSTANCE = new JavaReport();

    private final WebTauTestList tests = new WebTauTestList();

    private long startTime;
    private long stopTime;

    private JavaReport() {
    }

    public void clear() {
        tests.clear();
    }

    public boolean isEmpty() {
        return tests.isEmpty();
    }

    public void startTimer() {
        startTime = Time.currentTimeMillis();
    }

    public void addTest(WebTauTest test) {
        tests.add(test);
    }

    public void stopTimer() {
        stopTime = Time.currentTimeMillis();
    }

    public WebTauReport create() {
        return new WebTauReport(new WebTauReportName(getCfg().getReportName(), getCfg().getReportNameUrl()),
                tests, startTime, stopTime);
    }
}
