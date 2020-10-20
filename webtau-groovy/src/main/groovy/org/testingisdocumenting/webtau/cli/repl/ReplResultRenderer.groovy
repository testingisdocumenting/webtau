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

package org.testingisdocumenting.webtau.cli.repl

import org.apache.groovy.groovysh.Groovysh
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.testingisdocumenting.webtau.browser.page.PageUrl
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.cli.repl.tabledata.ReplTableRenderer
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.table.TableData
import org.testingisdocumenting.webtau.http.datanode.DataNode
import org.testingisdocumenting.webtau.http.render.DataNodeAnsiPrinter
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder
import org.testingisdocumenting.webtau.reporter.TokenizedMessageToAnsiConverter

import java.util.stream.Stream

import static org.testingisdocumenting.webtau.console.ConsoleOutputs.out

class ReplResultRenderer {
    private final Groovysh groovysh
    private final TokenizedMessageToAnsiConverter toAnsiConverter

    ReplResultRenderer(Groovysh groovysh) {
        this.groovysh = groovysh
        toAnsiConverter = IntegrationTestsMessageBuilder.getConverter()
    }

    void renderResult(Object result) {
        if (result instanceof WebTauConfig) {
            result.printAll()
        } else if (result instanceof DataNode) {
            renderDataNodeResult(result)
        } else if (result instanceof PageElement) {
            renderPageElementResult(result)
        } else if (result instanceof TableData) {
            renderTableData(result)
        } else if (result instanceof PageUrl) {
            renderPageUrl(result)
        } else {
            groovysh.defaultResultHook(result)
        }
    }

    private static void renderTableData(TableData tableData) {
        out(ReplTableRenderer.render(tableData))
    }

    private static void renderDataNodeResult(DataNode result) {
        new DataNodeAnsiPrinter().print(result)
    }

    private static void renderPageUrl(PageUrl pageUrl) {
        out(Color.YELLOW, " full: ", Color.GREEN, pageUrl.full.get())
        out(Color.YELLOW, " path: ", Color.GREEN, pageUrl.path.get())
        out(Color.YELLOW, "query: ", Color.GREEN, pageUrl.query.get())
        out(Color.YELLOW, "  ref: ", Color.GREEN, pageUrl.ref.get())
    }

    private void renderPageElementResult(PageElement pageElement) {
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
}
