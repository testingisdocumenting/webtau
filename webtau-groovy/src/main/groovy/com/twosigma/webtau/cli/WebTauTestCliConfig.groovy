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

    private List<String> testFiles
    private String env
    private Path configPath
    private CommandLine commandLine
    private ConfigObject configObject

    WebTauTestCliConfig(String... args) {
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

        if (commandLine.hasOption("help") || args.length < 1) {
            HelpFormatter helpFormatter = new HelpFormatter()
            helpFormatter.printHelp("webtau [options] [testFile1] [testFile2]", options)
            System.exit(1)
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
}
