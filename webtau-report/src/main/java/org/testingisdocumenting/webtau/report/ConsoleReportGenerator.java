/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.report;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.reporter.*;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;
import org.testingisdocumenting.webtau.utils.TimeUtils;

public class ConsoleReportGenerator implements ReportGenerator {
    private final static ConsoleStepReporter consoleStepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter(),
            () -> Integer.MAX_VALUE);

    @Override
    public void generate(WebTauReport report) {
        printTestsWithStatus(report, TestStatus.Errored);
        printTestsWithStatus(report, TestStatus.Failed);
        printTimeTaken(report);
        printTotals(report);
    }

    private static void printTestsWithStatus(WebTauReport report, TestStatus status) {
        if (report.getTests().countWithStatus(status) == 0) {
            return;
        }

        ConsoleOutputs.out(Color.BLUE, status.toString() + " tests:");
        report.getTests()
                .withStatus(status)
                .forEach(ConsoleReportGenerator::printFailedTest);
    }

    private static void printFailedTest(WebTauTest testEntry) {
        ConsoleOutputs.out(Color.RED, "[x] ", testEntry.getScenario(), Color.PURPLE, " ",
                testEntry.getFilePath());

        testEntry.findFirstFailedStep().ifPresent(ConsoleReportGenerator::printFailedStep);

        ConsoleOutputs.out(WebTauConfig.getCfg().getFullStackTrace() ?
                StackTraceUtils.renderStackTrace(testEntry.getException()) :
                StackTraceUtils.renderStackTraceWithoutLibCalls(testEntry.getException()), "\n");
    }

    private static void printFailedStep(WebTauStep step) {
        consoleStepReporter.onStepFailure(step);
        step.findFailedChildStep().ifPresent(ConsoleReportGenerator::printFailedStep);
    }

    private static void printTimeTaken(WebTauReport report) {
        ConsoleOutputs.out(Color.BLUE, "Total time: ", Color.PURPLE,
                TimeUtils.renderMillisHumanReadable(report.getDuration()));
    }

    private static void printTotals(WebTauReport report) {
        ConsoleOutputs.out("Total: ", report.getTotal(), ", ",
                Color.GREEN, " Passed: ", report.getPassed(), ", ",
                Color.YELLOW, " Skipped: ", report.getSkipped(), ", ",
                Color.RED, " Failed: ", report.getFailed(), ", ",
                " Errored: ", report.getErrored());
    }
}
