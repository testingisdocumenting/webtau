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
    @Override
    void beforeTestRun(StandaloneTest test) {
        ConsoleOutputs.out(Color.BLUE, test.scenario.trim(), ' ', Color.PURPLE, '(' + test.filePath + ')')
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        if (test.isFailed()) {
            outAfter(Color.RED, '[x]', test)
        } else if (test.hasError()) {
            outAfter(Color.RED, '[~]', test)
        } else if (test.isSkipped()) {
            outAfter(Color.YELLOW, '[o]', test)
        } else {
            outAfter(Color.GREEN, '[.]', test)
        }

        ConsoleOutputs.out()
    }

    private static void outAfter(Color color, String icon, StandaloneTest test) {
        ConsoleOutputs.out(color, icon, ' ', Color.BLUE, test.scenario.trim(),
                ' ', Color.PURPLE, '(' + test.filePath + ')')
    }
}
