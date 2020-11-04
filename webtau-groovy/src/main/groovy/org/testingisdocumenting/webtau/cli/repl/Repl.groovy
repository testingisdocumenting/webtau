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

import org.apache.groovy.groovysh.Groovysh
import org.codehaus.groovy.tools.shell.IO
import org.codehaus.groovy.tools.shell.util.Preferences
import org.testingisdocumenting.webtau.cfg.WebTauGroovyFileConfigHandler
import org.testingisdocumenting.webtau.cli.repl.win.WindowsOsReplFixes
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class Repl {
    private final Groovysh groovysh
    private final StandaloneTestRunner runner
    private final InteractiveTests interactiveTests
    private final ReplResultRenderer resultRenderer

    Repl(StandaloneTestRunner runner) {
        this.runner = runner
        interactiveTests = new InteractiveTests(runner)
        ReplCommands.interactiveTests = interactiveTests

        initHandlers()
        initConfig()
        groovysh = createShell()

        resultRenderer = new ReplResultRenderer(groovysh)
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
        WindowsOsReplFixes.apply()

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

        resultRenderer.renderResult(result)
    }

    private static void setDefaultReportPath() {
        if (!cfg.reportPathConfigValue.isDefault()) {
            return
        }

        cfg.reportPathConfigValue.set('repl',
                cfg.reportPath.toAbsolutePath().parent.resolve('webtau.repl.report.html'))
    }
}
