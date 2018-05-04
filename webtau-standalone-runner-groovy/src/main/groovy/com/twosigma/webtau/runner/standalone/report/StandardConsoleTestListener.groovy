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

package com.twosigma.webtau.runner.standalone.report

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListenerAdapter

class StandardConsoleTestListener extends StandaloneTestListenerAdapter {
    private int passed
    private int failed
    private int errored
    private int skipped

    @Override
    void beforeTestRun(StandaloneTest test) {
        ConsoleOutputs.out(Color.GREEN, test.description.trim())
        ConsoleOutputs.out(Color.PURPLE, test.filePath, "\n")
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        if (test.isFailed()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "failed")
            displayStackTrace(test.exception)
            failed++
        } else if (test.hasError()) {
            ConsoleOutputs.out(Color.RED, "[~] ", Color.BLUE, "error")
            displayStackTrace(test.exception)
            errored++
        } else if (test.isSkipped()) {
            ConsoleOutputs.out(Color.YELLOW, "[o] ", Color.BLUE, "skipped")
            skipped++
        } else {
            ConsoleOutputs.out(Color.GREEN, "[.] ", Color.BLUE, "passed")
            passed++
        }
    }

    int getTotal() {
        return passed + failed + errored + skipped
    }

    int getPassed() {
        return passed
    }

    int getFailed() {
        return failed
    }

    int getErrored() {
        return errored
    }

    int getSkipped() {
        return skipped
    }

    @Override
    void afterAllTests() {
        ConsoleOutputs.out()
        ConsoleOutputs.out("Total: ", getTotal(), ", ",
                Color.GREEN, " Passed: ", passed, ", ",
                Color.YELLOW, " Skipped: ", skipped, ", ",
                Color.RED, " Failed: ", failed, ", ",
                " Errored: ", errored)
    }

    private static void displayStackTrace(Throwable t) {
        ConsoleOutputs.out(GroovyStackTraceUtils.renderStackTraceWithoutLibCalls(t), "\n\n")
    }
}
