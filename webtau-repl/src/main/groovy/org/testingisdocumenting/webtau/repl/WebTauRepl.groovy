/*
 * Copyright 2021 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package org.testingisdocumenting.webtau.repl

import org.jline.builtins.ConfigurationPath
import org.jline.console.ConsoleEngine
import org.jline.console.Printer
import org.jline.console.impl.Builtins
import org.jline.console.impl.ConsoleEngineImpl
import org.jline.console.impl.DefaultPrinter
import org.jline.console.impl.SystemRegistryImpl
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.script.GroovyCommand
import org.jline.script.GroovyEngine
import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.OSUtils
import org.jline.widget.TailTipWidgets
import org.testingisdocumenting.webtau.GroovyRunner
import org.testingisdocumenting.webtau.cfg.WebTauGroovyFileConfigHandler
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicReference

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*

class WebTauRepl {
    private ExecutorService replExecutorService = Executors.newSingleThreadExecutor()
    private AtomicReference<Future> lastSubmittedCommand = new AtomicReference<>()

    private final InteractiveTests interactiveTests

    private String replRoot
    private ConfigurationPath configPath

    private DefaultParser parser
    private Terminal terminal
    private GroovyEngine scriptEngine
    private Printer printer
    private ConsoleEngineImpl consoleEngine
    private SystemRegistryImpl systemRegistry
    private Builtins builtins
    private LineReader reader
    private WebTauReplResultRenderer resultRenderer

    WebTauRepl(StandaloneTestRunner runner) {
        runner.setIsReplMode(true)
        interactiveTests = new InteractiveTests(runner)
        ReplCommands.interactiveTests = interactiveTests

        initHandlers()
        initConfig()

        prepareJLineRepl()
    }

    private void prepareJLineRepl() {
        createConfigPath()
        createParser()
        createTerminal()
        createPrinter()
        createScriptEngine()
        createConsoleEngine()
        createBuiltins()
        createSystemRegistry()
        createLineReader()
        createWidgets()
        createResultRenderer()
    }

    private void createConfigPath() {
        def webtauHomeDir = Paths.get(System.getProperty("user.home")).resolve(".webtau").toAbsolutePath()
        FileUtils.createDirs(webtauHomeDir)

        replRoot = webtauHomeDir.toString()
        configPath = new ConfigurationPath(Paths.get(replRoot), Paths.get(replRoot))
    }

    private void createParser() {
        parser = new DefaultParser()
        parser.setEofOnUnclosedBracket(DefaultParser.Bracket.CURLY, DefaultParser.Bracket.ROUND, DefaultParser.Bracket.SQUARE)
        parser.setEofOnUnclosedQuote(true)
        parser.setEscapeChars(null)
        parser.setRegexCommand("[:]{0,1}[a-zA-Z!]{1,}\\S*")    // change default regex to support shell commands
    }

    private void createTerminal() {
        terminal = TerminalBuilder.builder().build()
        if (terminal.getWidth() == 0 || terminal.getHeight() == 0) {
            terminal.setSize(new Size(120, 40))   // hard coded terminal size when redirecting
        }
        terminal.handle(Terminal.Signal.INT,
                signal -> {
                    def command = lastSubmittedCommand.get()
                    if (command != null) {
                        command.cancel(true)
                    }
                })
    }

    private void createPrinter() {
        printer = new DefaultPrinter(scriptEngine, configPath)
    }

    private void createScriptEngine() {
        String root = Paths.get("").toAbsolutePath().toString()

        scriptEngine = new WebTauGroovyEngine()
        scriptEngine.put("ROOT", root)
        scriptEngine.execute("import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*")
        scriptEngine.execute("import static org.testingisdocumenting.webtau.repl.ReplCommands.*")
        scriptEngine.execute("import static org.testingisdocumenting.webtau.repl.ReplHttpLastValidationCapture.*")
    }

    private void createConsoleEngine() {
        consoleEngine = new ConsoleEngineImpl(scriptEngine, printer,
                WebTauRepl::workDir,
                configPath)
    }

    private void createBuiltins() {
        builtins = new Builtins(WebTauRepl::workDir,
                configPath,
                (String fun) -> new ConsoleEngine.WidgetCreator(consoleEngine, fun))
    }

    private void createSystemRegistry() {
        systemRegistry = new SystemRegistryImpl(parser, terminal, WebTauRepl::workDir, configPath)
        systemRegistry.register("groovy", new GroovyCommand(scriptEngine, printer))
        systemRegistry.setCommandRegistries(consoleEngine, builtins)
        systemRegistry.addCompleter(scriptEngine.getScriptCompleter())
        systemRegistry.setScriptDescription(scriptEngine::scriptDescription)
    }

    private void createLineReader() {
        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(systemRegistry.completer())
                .parser(parser)
                .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
                .variable(LineReader.INDENTATION, 2)
                .variable(LineReader.LIST_MAX, 100)
                .variable(LineReader.HISTORY_FILE, Paths.get(replRoot, "history"))
                .option(LineReader.Option.INSERT_BRACKET, true)
                .option(LineReader.Option.EMPTY_WORD_OPTIONS, false)
                .option(LineReader.Option.USE_FORWARD_SLASH, true)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .build()

        if (OSUtils.IS_WINDOWS) {
            reader.setVariable(LineReader.BLINK_MATCHING_PAREN, 0)
        }

        consoleEngine.setLineReader(reader)
        builtins.setLineReader(reader)
    }

    private void createWidgets() {
        new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TailTipWidgets.TipType.COMBINED)
    }

    private void createResultRenderer() {
        resultRenderer = new WebTauReplResultRenderer()
    }

    private static Path workDir() {
        return Paths.get(System.getProperty("user.dir"))
    }

    private void run() {
        printPrompts()

        try {
            boolean terminated
            while (true) {
                terminated = readAndHandleLineTrueIfTerminated()
                if (terminated) {
                    println "<< terminated"
                    break
                }
            }

            systemRegistry.close()
            shutdownGroovyUiIfAny()

            System.exit(terminated ? 1 :0)
        }
        catch (Throwable t) {
            t.printStackTrace()
        }
    }

    private void printPrompts() {
        if (!interactiveTests.testFilePaths.isEmpty()) {
            ReplCommands.getList()
        }
    }

    private boolean readAndHandleLineTrueIfTerminated() {
        // based on examples and wiki in https://github.com/jline/jline3
        try {
            systemRegistry.cleanUp()
            String line = reader.readLine("webtau> ")
            line = parser.getCommand(line).startsWith("!") ? line.replaceFirst("!", "! ") : line

            def calcResultFuture = replExecutorService.submit(
                    (() -> systemRegistry.execute(line)) as Callable)
            lastSubmittedCommand.set(calcResultFuture)

            def renderResultFuture = replExecutorService.submit(
                    () -> resultRenderer.renderResult(calcResultFuture.get()))
            renderResultFuture.get()
        }
        catch (UserInterruptException ignored) {
            // Ignore
        }
        catch (EndOfFileException e) {
            String pl = e.getPartialLine()
            if (pl != null) {                 // execute last line from redirected file (required for Windows)
                try {
                    consoleEngine.println(systemRegistry.execute(pl))
                } catch (Exception e2) {
                    systemRegistry.trace(e2)
                }
            }
            return true
        }
        catch (Exception e) {
            systemRegistry.trace(e)          // print exception and save it to console variable
        }

        return false
    }

    void shutdownGroovyUiIfAny() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet()
        boolean groovyRunning = false              // check Groovy GUI apps
        for (Thread t : threadSet) {
            if (t.getName().startsWith("AWT-Shut")) {
                groovyRunning = true
                break
            }
        }
        if (groovyRunning) {
            consoleEngine.println("Please, close Groovy Consoles/Object Browsers!")
        }
    }

    static void main(String[] args) {
        def runner = new StandaloneTestRunner(
                GroovyRunner.createWithoutDelegating(cfg.workingDir),
                cfg.getWorkingDir())

        def repl = new WebTauRepl(runner)
        repl.run()
    }

    private static void initHandlers() {
        HttpValidationHandlers.add(new ReplHttpLastValidationCapture())
    }

    private static void initConfig() {
        WebTauGroovyFileConfigHandler.forceIgnoreErrors()
        setDefaultReportPath()
    }

    private static void setDefaultReportPath() {
        if (!cfg.reportPathConfigValue.isDefault()) {
            return
        }

        cfg.reportPathConfigValue.set('repl',
                cfg.reportPath.toAbsolutePath().parent.resolve('webtau.repl.report.html'))
    }
}