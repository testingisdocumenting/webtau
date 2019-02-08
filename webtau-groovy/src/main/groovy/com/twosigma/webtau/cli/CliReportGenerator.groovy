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

package com.twosigma.webtau.cli

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.report.Report
import com.twosigma.webtau.report.ReportGenerator
import com.twosigma.webtau.report.ReportTestEntry
import com.twosigma.webtau.reporter.TestStatus
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils

class CliReportGenerator implements ReportGenerator {
    @Override
    void generate(Report report) {
        printTestsWithStatus(report, TestStatus.Errored)
        printTestsWithStatus(report, TestStatus.Failed)
        printTotals(report)
    }

    private static void printTestsWithStatus(Report report, TestStatus status) {
        if (report.testEntries.countWithStatus(status) == 0) {
            return
        }

        ConsoleOutputs.out(Color.BLUE, status.toString() + ' tests:')
        report.testEntries
                .withStatus(status)
                .each { te -> printFailedTest(te) }
    }

    private static void printFailedTest(ReportTestEntry testEntry) {
        ConsoleOutputs.out(Color.RED, '[x] ', testEntry.scenario, Color.PURPLE, ' ',
                testEntry.filePath)

        ConsoleOutputs.out(StackTraceUtils.renderStackTraceWithoutLibCalls(testEntry.exception), '\n')
    }

    private static void printTotals(Report report) {
        def summary = report.createSummary()

        ConsoleOutputs.out('Total: ', summary.getTotal(), ', ',
                Color.GREEN, ' Passed: ', summary.passed, ', ',
                Color.YELLOW, ' Skipped: ', summary.skipped, ', ',
                Color.RED, ' Failed: ', summary.failed, ', ',
                ' Errored: ', summary.errored)

    }
}
