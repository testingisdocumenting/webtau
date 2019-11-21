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
import com.twosigma.webtau.reporter.WebTauReport
import com.twosigma.webtau.report.ReportGenerator
import com.twosigma.webtau.reporter.WebTauTest
import com.twosigma.webtau.reporter.TestStatus
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils

class CliReportGenerator implements ReportGenerator {
    @Override
    void generate(WebTauReport report) {
        printTestsWithStatus(report, TestStatus.Errored)
        printTestsWithStatus(report, TestStatus.Failed)
        printTotals(report)
    }

    private static void printTestsWithStatus(WebTauReport report, TestStatus status) {
        if (report.tests.countWithStatus(status) == 0) {
            return
        }

        ConsoleOutputs.out(Color.BLUE, status.toString() + ' tests:')
        report.tests
                .withStatus(status)
                .each { te -> printFailedTest(te) }
    }

    private static void printFailedTest(WebTauTest testEntry) {
        ConsoleOutputs.out(Color.RED, '[x] ', testEntry.scenario, Color.PURPLE, ' ',
                testEntry.filePath)

        ConsoleOutputs.out(StackTraceUtils.renderStackTraceWithoutLibCalls(testEntry.exception), '\n')
    }

    private static void printTotals(WebTauReport report) {
        ConsoleOutputs.out('Total: ', report.total, ', ',
                Color.GREEN, ' Passed: ', report.passed, ', ',
                Color.YELLOW, ' Skipped: ', report.skipped, ', ',
                Color.RED, ' Failed: ', report.failed, ', ',
                ' Errored: ', report.errored)
    }
}
