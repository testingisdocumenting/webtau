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

import org.testingisdocumenting.webtau.cfg.ConfigValue
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler

import java.nio.file.Path
import java.util.stream.Stream

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare

class StandaloneRunnerConfig implements WebTauConfigHandler {
    private static final ConfigValue consoleOutputCaptureDir = declare("consoleOutputCaptureDir",
            "path to a directory where overall and per test console output will be stored",
            () -> "")

    static boolean isConsoleOutputDirSet() {
        return !consoleOutputCaptureDir.isDefault()
    }

    static Path generateFullConsoleOutputPath() {
        return WebTauConfig.cfg.fullPath(consoleOutputCaptureDir.asString).resolve("all")
    }

    static Path generateScenarioConsoleOutputPath(Path testFilePath, String scenario) {
        def pathNoSeparators = testFilePath.toString().replace(File.separatorChar, '.' as char)
        def scenarioNoSpaces = scenario.replaceAll("\\s+", "-")

        return WebTauConfig.cfg.fullPath(consoleOutputCaptureDir.asString).resolve(
                pathNoSeparators + "-" + scenarioNoSpaces)
    }

    @Override
    Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(consoleOutputCaptureDir)
    }
}
