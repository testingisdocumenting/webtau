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

import org.jline.console.ConsoleEngine
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.testingisdocumenting.webtau.data.render.PrettyPrintable
import org.testingisdocumenting.webtau.db.DbQuery
import org.testingisdocumenting.webtau.fs.FileTextContent
import org.testingisdocumenting.webtau.repl.tabledata.ReplTableRenderer

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*
import static org.testingisdocumenting.webtau.console.ConsoleOutputs.*

class WebtauReplResultRenderer {
    private final ConsoleEngine consoleEngine

    WebtauReplResultRenderer(ConsoleEngine consoleEngine) {
        this.consoleEngine = consoleEngine
    }

    void renderResult(Object result) {
        if (result instanceof PageElement) {
            renderPageElementAndHighlight(result)
        } else if (result instanceof DbQuery) {
            renderDbQueryResult(result)
        } else if (result instanceof FileTextContent) {
            renderTextLimitingSize(result.data)
        } else if (result instanceof PrettyPrintable) {
            result.prettyPrint(asCombinedConsoleOutput())
        } else {
            consoleEngine.println(result)
        }
    }

    private static void renderTextLimitingSize(String text) {
        outLinesWithLimit(
                Arrays.asList(text.split("\n")),
                cfg.consolePayloadOutputLimit) {[it] as Object[]}
    }

    private static void renderDbQueryResult(DbQuery queryResult) {
        out(ReplTableRenderer.render(queryResult.tableData()))
    }

    private static void renderPageElementAndHighlight(PageElement pageElement) {
        pageElement.prettyPrint(asCombinedConsoleOutput())
        pageElement.highlight()
    }
}
