/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.repl

import org.apache.groovy.groovysh.Groovysh
import org.codehaus.groovy.tools.shell.IO
import org.jline.builtins.ConfigurationPath
import org.jline.builtins.Nano
import org.jline.console.ConsoleEngine
import org.jline.console.Printer
import org.jline.console.impl.Builtins
import org.jline.console.impl.ConsoleEngineImpl
import org.jline.console.impl.DefaultPrinter
import org.jline.console.impl.SystemHighlighter
import org.jline.console.impl.SystemRegistryImpl
import org.jline.keymap.KeyMap
import org.jline.reader.Binding
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.Reference
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.script.GroovyCommand
import org.jline.script.GroovyEngine
import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.OSUtils
import org.jline.widget.TailTipWidgets
import org.jline.widget.Widgets

import java.nio.file.Path
import java.nio.file.Paths

class WebtauRepl {
    private static Path workDir() {
        return Paths.get(System.getProperty("user.dir"));
    }

    static void main(String[] args) {
        trySomething()
    }

    static void trySomething() {
        def shell = new Groovysh(new IO())
        shell.run("abc.Hello.hello()")
    }

    static void startRepl() {
        try {
            //
            // Parser & Terminal
            //
            DefaultParser parser = new DefaultParser();
            parser.setEofOnUnclosedBracket(DefaultParser.Bracket.CURLY, DefaultParser.Bracket.ROUND, DefaultParser.Bracket.SQUARE);
            parser.setEofOnUnclosedQuote(true);
            parser.setEscapeChars(null);
            parser.setRegexCommand("[:]{0,1}[a-zA-Z!]{1,}\\S*");    // change default regex to support shell commands
            Terminal terminal = TerminalBuilder.builder().build();
            if (terminal.getWidth() == 0 || terminal.getHeight() == 0) {
                terminal.setSize(new Size(120, 40));   // hard coded terminal size when redirecting
            }
            Thread executeThread = Thread.currentThread();
            terminal.handle(Terminal.Signal.INT, signal -> executeThread.interrupt());
            //
            // Create jnanorc config file for demo
            //
//            File file = new File(WebtauRepl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
//            String root = file.getCanonicalPath().replace("classes", "").replaceAll("\\\\", "/"); // forward slashes works better also in windows!
//            File jnanorcFile = Paths.get("", "jnanorc").toFile();
//            if (!jnanorcFile.exists()) {
//                FileWriter fw = new FileWriter(jnanorcFile);
//                fw.write("include " + root + "nanorc/*.nanorc\n");
//                fw.close();
//            }
            //
            // ScriptEngine and command registries
            //
            String root = Paths.get("").toAbsolutePath().toString()

            GroovyEngine scriptEngine = new WebtauGroovyEngine()
            scriptEngine.put("ROOT", root);
            ConfigurationPath configPath = new ConfigurationPath(Paths.get(root), Paths.get(root));
            Printer printer = new DefaultPrinter(scriptEngine, configPath);
            ConsoleEngineImpl consoleEngine = new ConsoleEngineImpl(scriptEngine
                    , printer
                    , WebtauRepl::workDir, configPath);
            Builtins builtins = new Builtins(WebtauRepl::workDir, configPath, (String fun) -> new ConsoleEngine.WidgetCreator(consoleEngine, fun));
            SystemRegistryImpl systemRegistry = new SystemRegistryImpl(parser, terminal, WebtauRepl::workDir, configPath);
            systemRegistry.register("groovy", new GroovyCommand(scriptEngine, printer));
            systemRegistry.setCommandRegistries(consoleEngine, builtins);
            systemRegistry.addCompleter(scriptEngine.getScriptCompleter());
            systemRegistry.setScriptDescription(scriptEngine::scriptDescription);
            //
            // LineReader
            //

            Path jnanorc = Paths.get("/Users/mykolagolubyev/work/testingisdocumenting/webtau/webtau-repl/repl.nanorc")
            Path argsNano = Paths.get("/Users/mykolagolubyev/work/testingisdocumenting/webtau/webtau-repl/args.nanorc")
            Path commandNano = Paths.get("/Users/mykolagolubyev/work/testingisdocumenting/webtau/webtau-repl/command.nanorc")
            Path groovyNano = Paths.get("/Users/mykolagolubyev/work/testingisdocumenting/webtau/webtau-repl/groovy.nanorc")

            Nano.SyntaxHighlighter commandHighlighter = Nano.SyntaxHighlighter.build(commandNano.toUri().toString());
            Nano.SyntaxHighlighter argsHighlighter = Nano.SyntaxHighlighter.build(argsNano.toUri().toString());
            Nano.SyntaxHighlighter groovyHighlighter = Nano.SyntaxHighlighter.build(groovyNano.toUri().toString());

//            Nano.SyntaxHighlighter commandHighlighter = Nano.SyntaxHighlighter.build(jnanorc,"COMMAND");
//            Nano.SyntaxHighlighter argsHighlighter = Nano.SyntaxHighlighter.build(jnanorc,"ARGS");
//            Nano.SyntaxHighlighter groovyHighlighter = Nano.SyntaxHighlighter.build(jnanorc,"Groovy");
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(systemRegistry.completer())
                    .parser(parser)
                    .highlighter(new SystemHighlighter(commandHighlighter, argsHighlighter, groovyHighlighter))
                    .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
                    .variable(LineReader.INDENTATION, 2)
                    .variable(LineReader.LIST_MAX, 100)
                    .variable(LineReader.HISTORY_FILE, Paths.get(root, "history"))
                    .option(LineReader.Option.INSERT_BRACKET, true)
                    .option(LineReader.Option.EMPTY_WORD_OPTIONS, false)
                    .option(LineReader.Option.USE_FORWARD_SLASH, true)             // use forward slash in directory separator
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                    .build();
            if (OSUtils.IS_WINDOWS) {
                reader.setVariable(LineReader.BLINK_MATCHING_PAREN, 0);
                // if enabled cursor remains in begin parenthesis (gitbash)
            }
            //
            // complete command registries
            //
            consoleEngine.setLineReader(reader);
            builtins.setLineReader(reader);
            //
            // widgets and console initialization
            //
            new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
            KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
            keyMap.bind(new Reference(Widgets.TAILTIP_TOGGLE), KeyMap.alt("s"));
            systemRegistry.initialize(Paths.get(root, "init.jline").toFile());
            //
            // REPL-loop
            //
            while (true) {
                try {
                    systemRegistry.cleanUp();         // delete temporary variables and reset output streams
                    String line = reader.readLine("webtau> ");
                    line = parser.getCommand(line).startsWith("!") ? line.replaceFirst("!", "! ") : line;
                    Object result = systemRegistry.execute(line);
                    consoleEngine.println(result);
                }
                catch (UserInterruptException e) {
                    // Ignore
                }
                catch (EndOfFileException e) {
                    String pl = e.getPartialLine();
                    if (pl != null) {                 // execute last line from redirected file (required for Windows)
                        try {
                            consoleEngine.println(systemRegistry.execute(pl));
                        } catch (Exception e2) {
                            systemRegistry.trace(e2);
                        }
                    }
                    break;
                }
                catch (Exception e) {
                    systemRegistry.trace(e);          // print exception and save it to console variable
                }
            }
            systemRegistry.close();                   // persist pipeline completer names etc

            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            boolean groovyRunning = false;              // check Groovy GUI apps
            for (Thread t : threadSet) {
                if (t.getName().startsWith("AWT-Shut")) {
                    groovyRunning = true;
                    break;
                }
            }
            if (groovyRunning) {
                consoleEngine.println("Please, close Groovy Consoles/Object Browsers!");
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}