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

package com.twosigma.webtau.cli

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.cfg.WebTauConfig
import org.apache.commons.cli.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class WebTauTestCliConfig {
    private static final String CLI_SOURCE = "command line argument"
    private static final String CFG_SOURCE = "config file"

    private WebTauConfig cfg = WebTauConfig.INSTANCE

    private final ExitHandler exitHandler

    private List<String> testFiles
    private String env
    private Path configPath
    private CommandLine commandLine
    private ConfigObject configObject

    WebTauTestCliConfig(String... args) {
        this({ System.exit(it) }, args)
    }

    WebTauTestCliConfig(ExitHandler exitHandler, String... args) {
        this.exitHandler = exitHandler
        parseArgs(args)
    }

    List<String> getTestFiles() {
        return testFiles
    }

    void print() {
        cfg.print()
    }

    Closure httpHeaderProvider() {
        return configObject ? configObject.get("httpHeaderProvider") : null
    }

    void parseConfig(GroovyScriptEngine groovy) {
        if (! Files.exists(configPath)) {
            ConsoleOutputs.out("skipping config file as it is not found: ", Color.CYAN, configPath)
            return
        }

        ConfigSlurper configSlurper = new ConfigSlurper(cfg.env)
        def configScript = groovy.createScript(configPath.toAbsolutePath().toString(), new ConfigBinding())

        configObject = configSlurper.parse(configScript)

        if (configObject) {
            cfg.acceptConfigValues(CFG_SOURCE, configObject.flatten())
        }

        cfg.acceptConfigValues(CLI_SOURCE, commandLineArgsAsMap())
    }

    private void parseArgs(String[] args) {
        Options options = createOptions()
        commandLine = createCommandLine(args, options)

        if (commandLine.hasOption("help") || commandLine.argList.isEmpty()) {
            HelpFormatter helpFormatter = new HelpFormatter()
            helpFormatter.printHelp("webtau [options] [testFile1] [testFile2]", options)
            exitHandler.exit(1)
            return
        }

        testFiles = new ArrayList<>(commandLine.argList)
        Path workingDir = Paths.get(cliValue(cfg.getWorkingDirConfigName(), ""))
        configPath = workingDir.resolve(cliValue("config", "test.cfg"))

        def envCliValue = cliValue(cfg.envConfigValue.key, "local")
        cfg.acceptConfigValues(CLI_SOURCE, [(cfg.envConfigValue.key): envCliValue,
                                            (cfg.workingDirConfigValue.key): workingDir])
    }

    private static CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser()
        try {
            return parser.parse(options, args)
        } catch (ParseException e) {
            throw new RuntimeException(e)
        }
    }

    private def cliValue(String name, defaultValue) {
        return commandLine.hasOption(name) ? commandLine.getOptionValue(name) :
                defaultValue
    }

    private Options createOptions() {
        def options = new Options()
        options.addOption(null, "help", false, "print help")

        cfg.getCfgValuesStream().each {options.addOption(null, it.key, true, it.description)}
        return options
    }

    private Map commandLineArgsAsMap() {
        commandLine.options.collectEntries { [it.longOpt, it.value] }
    }

    interface ExitHandler {
        void exit(int status)
    }
}
