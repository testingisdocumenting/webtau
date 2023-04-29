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

package org.testingisdocumenting.webtau.data.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

class XmlParser extends DefaultHandler {
    private final List<XmlNode> result;
    private final Deque<List<XmlNode>> currentStack;
    private StringBuilder accumulatedText;

    public static XmlNode parse(String xmlContent) {
        return new XmlParser().convertXml(xmlContent);
    }

    private XmlParser() {
        currentStack = new ArrayDeque<>();
        result = new ArrayList<>();

        currentStack.add(result);
        accumulatedText = new StringBuilder();
    }

    private XmlNode convertXml(String xmlContent) {
        parseXml(xmlContent, this);
        return result.get(0);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        flushAccumulatedTextIfRequired();
        List<XmlNode> children = new ArrayList<>();
        currentStack.peekLast().add(new XmlNode(qName, children, parseAttributes(attributes), ""));
        currentStack.add(children);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        accumulatedText.append(new String(ch, start, length));
    }

    private XmlNode createTextNode(String text) {
        return new XmlNode("", Collections.emptyList(), Collections.emptyList(), text);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        flushAccumulatedTextIfRequired();
        currentStack.removeLast();
    }

    private void flushAccumulatedTextIfRequired() {
        String text = accumulatedText.toString();
        String trimmedText = text.trim();
        if (trimmedText.isEmpty()) {
            return;
        }

        boolean hasLeadingSpace = text.startsWith(" ") || text.startsWith("\n");
        boolean hasTrailingSpace = text.endsWith(" ") || text.endsWith("\n");

        String textToUse = (hasLeadingSpace ? " " : "") +
                trimmedText +
                (hasTrailingSpace ? " " : "");

        currentStack.peekLast().add(createTextNode(textToUse));
        accumulatedText = new StringBuilder();
    }

    private List<XmlAttribute> parseAttributes(Attributes attributes) {
        List<XmlAttribute> result = new ArrayList<>();

        int len = attributes.getLength();
        for (int i = 0; i < len; i++) {
            String name = attributes.getLocalName(i);
            String value = attributes.getValue(i);

            result.add(new XmlAttribute(name, value));
        }

        return result;
    }

    private static void parseXml(String xmlContent, DefaultHandler elementHandler) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);

            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)), elementHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
