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
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.reporter.*;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;
import org.testingisdocumenting.webtau.utils.TimeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsoleReportGenerator implements ReportGenerator {
    private final static int NUMBER_OF_FAILED_TESTS_TO_DISPLAY = 3;
    private final static int NUMBER_OF_ERRORED_TESTS_TO_DISPLAY = 3;
    private final static int NUMBER_OF_TESTS_TO_DISPLAY_EXTRA_THRESHOLD = 2;

    private final static int NUMBER_OF_WARNINGS_TO_DISPLAY = 3;
    private final static int NUMBER_OF_WARNINGS_TO_DISPLAY_EXTRA_THRESHOLD = 2;

    private final static ConsoleStepReporter consoleStepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter(),
            () -> Integer.MAX_VALUE);

    @Override
    public void generate(WebTauReport report) {
        ConsoleOutputs.out();

        printTestsWithStatus(report, TestStatus.Errored, NUMBER_OF_ERRORED_TESTS_TO_DISPLAY);
        printTestsWithStatus(report, TestStatus.Failed, NUMBER_OF_FAILED_TESTS_TO_DISPLAY);

        printWarnings(report);
        printTimeTaken(report);
        printTotals(report);
    }

    private static void printTestsWithStatus(WebTauReport report, TestStatus status, int limit) {
        String statusLabel = status.toString().toLowerCase();

        List<WebTauTest> withStatus = report.getTests().withStatus(status).collect(Collectors.toList());
        if (withStatus.isEmpty()) {
            return;
        }

        int testsLimit = withStatus.size() - limit > NUMBER_OF_TESTS_TO_DISPLAY_EXTRA_THRESHOLD ?
                limit:
                withStatus.size();

        ConsoleOutputs.out(Color.RED, "you have ", Color.BLUE, withStatus.size(),
                Color.RED, " ", statusLabel, " test(s):");

        withStatus.stream()
                .limit(testsLimit)
                .forEach(ConsoleReportGenerator::printFailedTest);

        if (withStatus.size() > testsLimit) {
            ConsoleOutputs.out(Color.YELLOW, "...(" + (withStatus.size() - testsLimit) + " more " + statusLabel + " tests)");
            ConsoleOutputs.out();
        }
    }

    private static void printFailedTest(WebTauTest testEntry) {
        ConsoleOutputs.out(Color.RED, "[x] ", testEntry.getScenario(), Color.PURPLE, " ",
                "(", testEntry.getShortContainerId(), ")");

        testEntry.findFirstFailedStep().ifPresent(ConsoleReportGenerator::printFailedStep);

        ConsoleOutputs.out(WebTauConfig.getCfg().getFullStackTrace() ?
                StackTraceUtils.renderStackTrace(testEntry.getException()) :
                StackTraceUtils.renderStackTraceWithoutLibCalls(testEntry.getException()), "\n");
    }

    private static void printFailedStep(WebTauStep step) {
        consoleStepReporter.printStepFailureWithoutOutput(step);
        step.findFailedChildStep().ifPresent(ConsoleReportGenerator::printFailedStep);
        consoleStepReporter.printStepOutput(step);
    }

    private void printWarnings(WebTauReport report) {
        WebTauReportCustomData warningsData = report.findCustomData(WarningsReportDataProvider.ID);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> warnings = (List<Map<String, Object>>) warningsData.getData();
        if (warnings.isEmpty()) {
            return;
        }

        // have a delta of warnings to display to avoid messages like (and 1 more) and instead show an extra one or two
        int warningsLimit = warnings.size() - NUMBER_OF_WARNINGS_TO_DISPLAY > NUMBER_OF_WARNINGS_TO_DISPLAY_EXTRA_THRESHOLD ?
                NUMBER_OF_WARNINGS_TO_DISPLAY :
                warnings.size();

        ConsoleOutputs.out(Color.RED, "There are ", Color.BLUE, warnings.size(), Color.RED, " warning(s) in tests");

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

        PrettyPrinter printer = new PrettyPrinter(ConsoleOutputs.asCombinedConsoleOutput(), 2);
        WebTauStepInputKeyValue.stepInput(input).prettyPrint(printer);
        printer.renderToConsole();
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
