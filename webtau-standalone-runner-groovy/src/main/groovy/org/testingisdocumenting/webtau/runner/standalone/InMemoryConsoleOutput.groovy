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

package org.testingisdocumenting.webtau.runner.standalone

import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Path

class InMemoryConsoleOutput implements ConsoleOutput {
    private final List<String> out = []
    private final List<String> err = []

    @Override
    void out(Object... styleOrValues) {
        out.add(fromAnsiStyles(styleOrValues))
    }

    @Override
    void err(Object... styleOrValues) {
        err.add(fromAnsiStyles(styleOrValues))
    }

    void store(Path path) {
        Path outPath = path.resolveSibling(path.fileName.toString() + ".out")
        Path errPath = path.resolveSibling(path.fileName.toString() + ".err")

        FileUtils.writeTextContent(outPath, String.join("\n", out))
        if (!err.isEmpty()) {
            FileUtils.writeTextContent(errPath, String.join("\n", err))
        }
    }

    private static String fromAnsiStyles(Object... styleOrValue) {
        return WebTauConfig.cfg.isAnsiEnabled() ?
            new AutoResetAnsiString(styleOrValue).toString():
            new IgnoreAnsiString(styleOrValue).toString()
    }
}
