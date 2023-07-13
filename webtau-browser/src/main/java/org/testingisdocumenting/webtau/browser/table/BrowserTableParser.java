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

package org.testingisdocumenting.webtau.browser.table;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.TableDataUnderscore;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BrowserTableParser {
    enum State {
        PARSING_HEADER,
        PARSING_BODY
    }

    private static final List<BrowserTableParserHandler> parserHandlers = discoverHandlers();

    private final BrowserTableParserHandler handler;
    private final String html;
    private final BrowserTableNode tableNode;

    private final List<String> headerValues;
    private final List<String> bodyValues;

    private State parsingState;


    private BrowserTableParser(BrowserTableParserHandler handler, String html) {
        this.handler = handler;
        this.html = html;
        this.tableNode = new BrowserTableNode();

        this.headerValues = new ArrayList<>();
        this.bodyValues = new ArrayList<>();

        this.parsingState = State.PARSING_HEADER;
    }

    public static TableData parse(String html) {
        BrowserTableParserHandler tableParserHandler = parserHandlers.stream()
                .filter(handler -> handler.handles(html))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can't find HTML table parser to handle:\n" + html));

        BrowserTableParser parser = new BrowserTableParser(tableParserHandler, html);
        return parser.parseAndBuildTable();
    }

    private TableData parseAndBuildTable() {
        Document document = Jsoup.parse(html);
        Element body = document.selectFirst("body");
        if (body == null) {
            throw new IllegalStateException("can't parse html:\n" + html);
        }

        Element first = body.firstElementChild();

        handleElement(first);

        return new TableData(Stream.concat(headerValues.stream(),
                Stream.concat(Stream.of(TableDataUnderscore.UNDERSCORE),
                        bodyValues.stream())));
    }

    private void handleElement(Element element) {
        tableNode.setElement(element);

        if (handler.isHeaderValue(tableNode)) {
            if (parsingState == State.PARSING_BODY) {
                throw new IllegalStateException("was already parsing body when encountered header");
            }

            parsingState = State.PARSING_HEADER;

            headerValues.add(element.text());
        } else if (handler.isBodyValue(tableNode)) {
            parsingState = State.PARSING_BODY;

            bodyValues.add(element.text());
        } else {
            element.children().forEach(this::handleElement);
        }
    }

    private static List<BrowserTableParserHandler> discoverHandlers() {
        ArrayList<BrowserTableParserHandler> result = new ArrayList<>();
        result.add(new BrowserTableParserStandardTableHandler());
        result.add(new BrowserTableParserAGGridTableHandler());
        result.addAll(ServiceLoaderUtils.load(BrowserTableParserHandler.class));

        return result;
    }
}
