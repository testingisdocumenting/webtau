/*
 * Copyright 2020 webtau maintainers
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
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.testingisdocumenting.webtau.data.render.PrettyPrinter
import org.testingisdocumenting.webtau.repl.tabledata.ReplTableRenderer
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.data.render.PrettyPrintable
import org.testingisdocumenting.webtau.db.DbQuery
import org.testingisdocumenting.webtau.fs.FileTextContent

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.cfg
import static org.testingisdocumenting.webtau.console.ConsoleOutputs.out

class ReplResultRenderer {
    private final Groovysh groovysh

    ReplResultRenderer(Groovysh groovysh) {
        this.groovysh = groovysh
    }

    void renderResult(Object result) {
        if (result instanceof PageElement) {
            renderPageElementAndHighlight(result)
        } else if (result instanceof DbQuery) {
            renderDbQueryResult(result)
        } else if (result instanceof FileTextContent) {
            renderTextLimitingSize(result.data)
        } else if (result instanceof PrettyPrintable) {
            def printer = createPrettyPrinter()
            result.prettyPrint(printer)
            printer.renderToConsole()
        } else {
            groovysh.defaultResultHook(result)
        }
    }

    private static void renderTextLimitingSize(String text) {
        ConsoleOutputs.outLinesWithLimit(
                Arrays.asList(text.split("\n")),
                cfg.consolePayloadOutputLimit) {[it] as Object[]}
    }

    private static void renderDbQueryResult(DbQuery queryResult) {
        out(ReplTableRenderer.render(queryResult.tableData()))
    }

    private static void renderPageElementAndHighlight(PageElement pageElement) {
        def printer = createPrettyPrinter()
        pageElement.prettyPrint(printer)
        printer.renderToConsole(ConsoleOutputs.asCombinedConsoleOutput())
        pageElement.highlight()
    }

    private static PrettyPrinter createPrettyPrinter() {
        return new PrettyPrinter(0)
    }
}
