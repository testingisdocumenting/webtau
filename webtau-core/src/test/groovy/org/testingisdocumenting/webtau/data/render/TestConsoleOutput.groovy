/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.render

import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString

class TestConsoleOutput implements ConsoleOutput {
    List<String> noColorLines = []
    List<String> colorLines = []

    void clear() {
        noColorLines.clear()
        colorLines.clear()
    }

    String getNoColorOutput() {
        return noColorLines.join("\n")
    }

    String getColorOutput() {
        return colorLines.join("\n")
    }

    @Override
    void out(Object... styleOrValues) {
        colorLines.add(new AutoResetAnsiString(styleOrValues).toString())
        noColorLines.add(new IgnoreAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {

    }
}
