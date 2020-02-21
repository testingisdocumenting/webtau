/*
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

package com.twosigma.webtau.cli.repl

import com.twosigma.webtau.browser.page.PageElement
import com.twosigma.webtau.cfg.WebTauGroovyFileConfigHandler
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.http.datanode.DataNode
import com.twosigma.webtau.http.render.DataNodeAnsiPrinter
import com.twosigma.webtau.http.validation.HttpValidationHandlers
import com.twosigma.webtau.reporter.ConsoleStepReporter
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TokenizedMessageToAnsiConverter
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.util.Preferences

import java.util.stream.Stream

class Repl {
    private final Groovysh groovysh
    private final TokenizedMessageToAnsiConverter toAnsiConverter


    Repl() {
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
        groovysh.run("")
    }

    private void initHandlers() {
        StepReporters.add(new ConsoleStepReporter(toAnsiConverter))
        HttpValidationHandlers.add(new ReplHttpLastValidationCapture())
    }

    private static void initConfig() {
        WebTauGroovyFileConfigHandler.forceIgnoreErrors()
    }

    private Groovysh createShell() {
        System.setProperty("groovysh.prompt", "webtau")

        def shell = new Groovysh()
        shell.imports << "static com.twosigma.webtau.WebTauGroovyDsl.*"
        shell.imports << "static com.twosigma.webtau.cli.repl.ReplCommands.*"
        shell.imports << "static com.twosigma.webtau.cli.repl.ReplHttpLastValidationCapture.*"
        shell.errorHook = this.&errorHook

        shell.resultHook = { Object result ->
            if (result != null) {
                renderResult(result)
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

        if (result instanceof DataNode) {
            showDataNodeResult(result)
        } else if (result instanceof PageElement) {
            showPageElementResult(result)
        } else {
            groovysh.defaultResultHook(result)
        }
    }

    private static void showDataNodeResult(DataNode result) {
        new DataNodeAnsiPrinter().print(result)
    }

    private void showPageElementResult(PageElement pageElement) {
        if (!pageElement.isPresent()) {
            ConsoleOutputs.out(Stream.concat(
                    Stream.of(Color.RED, "element is not present: "),
                    toAnsiConverter.convert(pageElement.locationDescription()).stream()).toArray())
            return
        }

        ConsoleOutputs.out(Stream.concat(
                Stream.of(Color.GREEN, "element is found: "),
                toAnsiConverter.convert(pageElement.locationDescription()).stream()).toArray())

        ConsoleOutputs.out(Color.YELLOW, "           getText(): ", Color.GREEN, pageElement.getText())
        ConsoleOutputs.out(Color.YELLOW, "getUnderlyingValue(): ", Color.GREEN, pageElement.getUnderlyingValue())
        def count = pageElement.count.get()
        if (count > 1) {
            ConsoleOutputs.out(Color.YELLOW, "               count: ", Color.GREEN, count)
        }

        pageElement.highlight()
    }
}
