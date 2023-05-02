/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.app.cfg

import org.testingisdocumenting.webtau.app.examples.ExamplesScaffolder
import org.testingisdocumenting.webtau.cfg.ConfigValue
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.version.WebTauVersion

import java.nio.file.Paths

class WebTauCliArgsConfig {
    private static final String REPL = "repl"
    private static final String REPL_EXPERIMENTAL = "replexp"
    private static final String REPL_OLD = "replold"

    private static final String CLI_SOURCE = "command line argument"

    private static final String HELP_KEY = "help"
    private static final String VERSION_KEY = "version"
    private static final String EXAMPLE_KEY = "example"

    private static final Set<String> COMMANDS = [REPL, REPL_EXPERIMENTAL, REPL_OLD]

    private final WebTauConfig cfg

    private final ExitHandler exitHandler

    private List<String> testFiles
    private CommandLine commandLine
    private String[] args

    WebTauCliArgsConfig(WebTauConfig cfg, String... args) {
        this(cfg, { System.exit(it) }, args)
    }

    WebTauCliArgsConfig(WebTauConfig cfg, ExitHandler exitHandler, String... args) {
        this.args = args
        this.cfg = cfg
        this.exitHandler = exitHandler
        parseArgs()
    }

    static boolean isReplMode(String[] args) {
        return args.any { it == REPL }
    }

    static boolean isExperimentalReplMode(String[] args) {
        return args.any { it == REPL_EXPERIMENTAL }
    }

    static boolean isOldReplMode(String[] args) {
        return args.any { it == REPL_OLD }
    }

    void setConfigFileRelatedCfgIfPresent() {
        setValueFromCliIfPresent(cfg.workingDirConfigValue)
        setValueFromCliIfPresent(cfg.configFileNameValue)
        setValueFromCliIfPresent(cfg.envConfigValue)
    }

    void setRestOfConfigValuesFromArgs() {
        cfg.acceptConfigValues(CLI_SOURCE, commandLineArgsAsMap())
    }

    List<String> getTestFiles() {
        return testFiles
    }

    private void parseArgs() {
        Options options = createOptions()
        commandLine = createCommandLine(args, options)

        if (commandLine.hasOption(EXAMPLE_KEY)) {
            scaffoldExamples()
        } else if (commandLine.hasOption(VERSION_KEY)) {
            printVersion()
        } else if (commandLine.hasOption(HELP_KEY) ||
                commandLine.argList.isEmpty() &&
                !isReplMode(args) && !isExperimentalReplMode(args)) {
            printHelp(options)
        } else {
            testFiles = new ArrayList<>(commandLine.argList.findAll { !COMMANDS.contains(it) })
        }
    }

    private void scaffoldExamples() {
        ExamplesScaffolder.scaffold(Paths.get(""))
        exitHandler.exit(0)
    }

    private void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter()

        WebTauVersion.print()
        helpFormatter.printHelp("webtau [options] [testFile1] [testFile2]", "", options, "")
        exitHandler.exit(1)
    }

    private void printVersion() {
        WebTauVersion.print()
        exitHandler.exit(1)
    }

    private static CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser()
        try {
            return parser.parse(options, args)
        } catch (ParseException e) {
            ConsoleOutputs.out(Color.RED, e.getMessage())
            ConsoleOutputs.out(Color.BLUE, "use --help to list all available parameters")
            System.exit(2)

            return null
        }
    }

    private def setValueFromCliIfPresent(ConfigValue configValue) {
        if (commandLine.hasOption(configValue.key)) {
            configValue.set(CLI_SOURCE, commandLine.getOptionValue(configValue.key))
        }
    }

    private Options createOptions() {
        def options = new Options()
        options.addOption(null, HELP_KEY, false, "print help")
        options.addOption(null, VERSION_KEY, false, "print version")
        options.addOption(null, EXAMPLE_KEY, false, "generate basic examples")

        cfg.getEnumeratedCfgValuesStream().each {
            options.addOption(null, it.key, !it.isBoolean(), it.description)
        }

        return options
    }

    private Map commandLineArgsAsMap() {
        commandLine.options.collectEntries { [
                it.longOpt,
                cfg.findConfigValue(it.longOpt).boolean ? true : it.value
        ] }
    }

    interface ExitHandler {
        void exit(int status)
    }
}
