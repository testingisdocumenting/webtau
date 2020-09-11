/*
 * Copyright 2020 webtau maintainers
 * Copyright 2020 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.cli.repl

import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.IO
import org.codehaus.groovy.tools.shell.util.Preferences
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.cfg.WebTauGroovyFileConfigHandler
import org.testingisdocumenting.webtau.cli.repl.tabledata.ReplTableRenderer
import org.testingisdocumenting.webtau.cli.repl.win.WindowsOsFixes
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.table.TableData
import org.testingisdocumenting.webtau.http.datanode.DataNode
import org.testingisdocumenting.webtau.http.render.DataNodeAnsiPrinter
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder
import org.testingisdocumenting.webtau.reporter.TokenizedMessageToAnsiConverter
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner

import java.util.stream.Stream

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg
import static org.testingisdocumenting.webtau.console.ConsoleOutputs.out

class Repl {
    private final Groovysh groovysh
    private final TokenizedMessageToAnsiConverter toAnsiConverter
    private final StandaloneTestRunner runner
    private final InteractiveTests interactiveTests

    Repl(StandaloneTestRunner runner) {
        this.runner = runner
        interactiveTests = new InteractiveTests(runner)
        ReplCommands.interactiveTests = interactiveTests

        toAnsiConverter = IntegrationTestsMessageBuilder.getConverter()
        initHandlers()
        initConfig()
        groovysh = createShell()
    }

    static void main(String[] args) {
        def repl = new Repl()
        repl.run()
    }

    void run() {
        def commandLine = interactiveTests.testFilePaths.isEmpty() ? "" : "ls"
        groovysh.run(commandLine)
    }

    private static void initHandlers() {
        HttpValidationHandlers.add(new ReplHttpLastValidationCapture())
    }

    private static void initConfig() {
        WebTauGroovyFileConfigHandler.forceIgnoreErrors()
        setDefaultReportPath()
    }

    private Groovysh createShell() {
        System.setProperty("groovysh.prompt", "webtau")
        WindowsOsFixes.apply()

        def shell = new Groovysh(new IO())
        shell.imports << "static org.testingisdocumenting.webtau.WebTauGroovyDsl.*"
        shell.imports << "static org.testingisdocumenting.webtau.cli.repl.ReplCommands.*"
        shell.imports << "static org.testingisdocumenting.webtau.cli.repl.ReplHttpLastValidationCapture.*"
        shell.errorHook = this.&errorHook

        shell.resultHook = { Object result ->
            if (result != null) {
                renderResult(result)
            }
        }

        addShutdownHook {
            if (shell.history) {
                shell.history.flush()
            }
        }

        return shell
    }

    private static void errorHook(Throwable error) {
        ConsoleOutputs.err("ERROR:\n" + StackTraceUtils.renderStackTraceWithoutLibCalls(error))
    }

    private void renderResult(result) {
        def io = groovysh.io

        boolean showLastResult = !io.quiet && (io.verbose ||
                Preferences.get(Groovysh.SHOW_LAST_RESULT_PREFERENCE_KEY, 'false'))

        if (!showLastResult) {
            return
        }

        if (result instanceof WebTauConfig) {
            result.printAll()
        } else if (result instanceof DataNode) {
            showDataNodeResult(result)
        } else if (result instanceof PageElement) {
            showPageElementResult(result)
        } else if (result instanceof TableData) {
            showTableData(result)
        } else {
            groovysh.defaultResultHook(result)
        }
    }

    private static void showTableData(TableData tableData) {
        println ReplTableRenderer.render(tableData)
    }

    private static void showDataNodeResult(DataNode result) {
        new DataNodeAnsiPrinter().print(result)
    }

    private void showPageElementResult(PageElement pageElement) {
        if (!pageElement.isPresent()) {
            out(Stream.concat(
                    Stream.of(Color.RED, "element is not present: "),
                    toAnsiConverter.convert(pageElement.locationDescription()).stream()).toArray())
            return
        }

        out(Stream.concat(
                Stream.of(Color.GREEN, "element is found: "),
                toAnsiConverter.convert(pageElement.locationDescription()).stream()).toArray())

        out(Color.YELLOW, "           getText(): ", Color.GREEN, pageElement.getText())
        out(Color.YELLOW, "getUnderlyingValue(): ", Color.GREEN, pageElement.getUnderlyingValue())
        def count = pageElement.count.get()
        if (count > 1) {
            out(Color.YELLOW, "               count: ", Color.GREEN, count)
        }

        pageElement.highlight()
    }

    private static void setDefaultReportPath() {
        if (!cfg.reportPathConfigValue.isDefault()) {
            return
        }

        cfg.reportPathConfigValue.set('repl',
                cfg.reportPath.toAbsolutePath().parent.resolve('webtau.repl.report.html'))
    }
}
