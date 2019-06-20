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

package com.twosigma.webtau.cfg

import com.twosigma.webtau.TestFile
import groovy.io.FileType

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

import static com.twosigma.webtau.cfg.ConfigValue.declare
import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauGroovyCliArgsConfigHandler implements WebTauConfigHandler {
    private static final ConfigValue numberOfThreads = declare("numberOfThreads",
            "number of threads on which to run test files (one file per thread)",
            { -> 1 })

    private String[] args
    private WebTauCliArgsConfig argsConfig

    WebTauGroovyCliArgsConfigHandler(String[] args) {
        this.args = args
    }

    @Override
    void onBeforeCreate(WebTauConfig cfg) {
    }

    @Override
    void onAfterCreate(WebTauConfig cfg) {
        /*
         * this config handler is registered twice. before any other handler, and then after all handlers.
         * We need to parse args first to get config file essential params first.
         * After it is done and config file handler is ran, we need to be able to override config values using args.
         * 1. get config related params
         * 2. read config file if present
         * 3. set other arg values on top as overrides
         */
        if (! argsConfig) {
            argsConfig = new WebTauCliArgsConfig(cfg, args)
            argsConfig.setConfigFileRelatedCfgIfPresent()
        } else {
            argsConfig.setRestOfConfigValuesFromArgs()
        }
    }

    List<TestFile> testFilesWithFullPath() {
        return argsConfig.testFiles.collectMany { fileName ->
            def path = cfg.workingDir.resolve(Paths.get(fileName)).toAbsolutePath()
            def testFiles = []
            if (Files.isDirectory(path)) {
                testFiles += discoverTestFiles(path)
            } else {
                testFiles << new TestFile(path, path.fileName.toString())
            }

            return testFiles
        }
    }

    static List<TestFile> discoverTestFiles(Path directory) {
        def testFiles = []
        int baseDirEndIdx = directory.toString().length()
        directory.toFile().eachFileRecurse(FileType.FILES) {
            Path testFilePath = it.toPath()
            if (testFilePath.toString().endsWith(".groovy")) {
                String shortName = testFilePath.toString().substring(baseDirEndIdx + 1)
                testFiles << new TestFile(testFilePath, shortName)
            }
        }

        return testFiles
    }

    @Override
    Stream<ConfigValue> additionalConfigValues() {
        return [numberOfThreads].stream()
    }

    static int getNumberOfThreads() {
        return numberOfThreads.getAsInt()
    }
}
