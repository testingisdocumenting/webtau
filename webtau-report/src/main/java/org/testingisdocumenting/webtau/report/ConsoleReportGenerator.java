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
import org.testingisdocumenting.webtau.console.IndentedConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.reporter.*;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;
import org.testingisdocumenting.webtau.utils.TimeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConsoleReportGenerator implements ReportGenerator {
    private final static int NUMBER_OF_WARNINGS_TO_DISPLAY = 3;
    private final static ConsoleStepReporter consoleStepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter(),
            () -> Integer.MAX_VALUE);

    @Override
    public void generate(WebTauReport report) {
        printTestsWithStatus(report, TestStatus.Errored);
        printTestsWithStatus(report, TestStatus.Failed);
        printWarnings(report);
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

    private void printWarnings(WebTauReport report) {
        WebTauReportCustomData warningsData = report.findCustomData(WarningsReportDataProvider.ID);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> warnings = (List<Map<String, Object>>) warningsData.getData();
        if (warnings.isEmpty()) {
            return;
        }

        // have a delta of warnings to display to avoid messages like (and 1 more) and instead show an extra one or two
        int warningsLimit = warnings.size() - NUMBER_OF_WARNINGS_TO_DISPLAY > 2 ?
                NUMBER_OF_WARNINGS_TO_DISPLAY :
                warnings.size();

        ConsoleOutputs.out(Color.RED, "There are ", Color.BLUE, warningsLimit, Color.RED, " warning(s) in tests");

        warnings.stream().limit(warningsLimit).forEach((warning) -> printWarning(report, warning));
        if (warnings.size() > warningsLimit) {
            ConsoleOutputs.out(Color.YELLOW, "...(" + (warnings.size() - warningsLimit) + " more warnings)");
        }
    }

    private void printWarning(WebTauReport report, Map<String, Object> warning) {
        String testId = warning.getOrDefault("testId", "").toString();
        String message = warning.getOrDefault("message", "").toString();
        @SuppressWarnings("unchecked")
        Map<String, Object> input = (Map<String, Object>) warning.getOrDefault("input", Collections.emptyMap());

        WebTauTest test = report.getTestById(testId);
        if (test == null) {
            throw new IllegalStateException("can't find test with id: " + testId);
        }

        ConsoleOutputs.out(Color.RED, "*", Color.YELLOW, " ", message, " ",
                Color.RESET, "(", Color.PURPLE, test.getShortContainerId(), " -> ", Color.BLUE, test.getScenario(), Color.RESET, ")");

        IndentedConsoleOutput indentedConsoleOutput = new IndentedConsoleOutput(ConsoleOutputs.asCombinedConsoleOutput(), 2);
        WebTauStepInputKeyValue.stepInput(input).prettyPrint(indentedConsoleOutput);
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
