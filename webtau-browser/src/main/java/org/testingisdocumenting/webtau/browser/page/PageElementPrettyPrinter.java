/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.xml.XmlPrettyPrinter;
import org.testingisdocumenting.webtau.reporter.TokenizedMessageToAnsiConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

class PageElementPrettyPrinter {
    private final PageElement pageElement;
    private final TokenizedMessageToAnsiConverter toAnsiConverter;
    private final int maxElementsToPrint;

    PageElementPrettyPrinter(PageElement pageElement, int maxElementsToPrint) {
        this.toAnsiConverter = TokenizedMessageToAnsiConverter.DEFAULT;
        this.pageElement = pageElement;
        this.maxElementsToPrint = maxElementsToPrint;
    }

    void prettyPrint(PrettyPrinter printer) {
        if (!pageElement.isPresent()) {
            printNotFound(printer);
            return;
        }

        printFoundSummary(printer);
        printElementDetails(printer);
    }

    private void printNotFound(PrettyPrinter printer) {
        printer.printLine(Stream.concat(
                Stream.of(Color.RED, "element is not present: "),
                toAnsiConverter.convert(ValueConverter.EMPTY, pageElement.locationDescription(), 0).stream()).toArray());
    }

    private void printFoundSummary(PrettyPrinter printer) {
        Integer count = pageElement.getCount().get();
        String countLabel = count > 1 ? count.toString() : "single";
        String elementsLabel = count > 1 ? "elements" : "element";
        printer.printLine(Stream.concat(
                Stream.of(Color.BLUE, "found ", countLabel, " ", elementsLabel, " using "),
                toAnsiConverter.convert(ValueConverter.EMPTY, pageElement.locationDescription(), 0).stream()).toArray());
    }

    private void printElementDetails(PrettyPrinter printer) {
        List<HtmlNode> htmlNodes = pageElement.extractHtmlNodes();
        int count = htmlNodes.size();

        int numberOfNonPrinted = Math.max(0, count - maxElementsToPrint);
        boolean printSeparators = count > 1;

        int nodeIdx = 0;
        for (HtmlNode htmlNode : htmlNodes) {
            if (nodeIdx >= maxElementsToPrint) {
                break;
            }

            if (printSeparators) {
                printer.printLine(Color.BLUE, "element #", (nodeIdx + 1));
            }

            printer.increaseIndentation();
            printElementDetails(printer, htmlNode);
            printer.decreaseIndentation();

            boolean isLastElement = nodeIdx == count - 1;
            if (printSeparators && !isLastElement) {
                printer.printLine();
            }

            nodeIdx++;
        }

        if (numberOfNonPrinted > 0) {
            printer.printLine(Color.YELLOW, "...", Color.BLUE, numberOfNonPrinted, " more");
        }
    }

    private static void printElementDetails(PrettyPrinter printer, HtmlNode htmlNode) {
        printer.printLine(Color.PURPLE, "innerText: ", Color.GREEN, htmlNode.innerText());
        if (htmlNode.tagName().equals("input")) {
            boolean isValueEmpty = htmlNode.value().isEmpty();
            printer.printLine(Color.PURPLE, "value: ", isValueEmpty ? Color.YELLOW : Color.GREEN,
                    isValueEmpty ? "<empty>" : htmlNode.value());
        }

        if (!htmlNode.outerHtml().isEmpty()) {
            String xml = normalizeHtml(htmlNode.outerHtml());
            new XmlPrettyPrinter(xml).prettyPrint(printer);

            printer.flushCurrentLine();
        }
    }

    private static String normalizeHtml(String html) {
        Document.OutputSettings outputSettings = new Document.OutputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .charset(StandardCharsets.UTF_8)
                .prettyPrint(false);

        return Jsoup.parse(html)
                .outputSettings(outputSettings)
                .select("body")
                .html();
    }
}
