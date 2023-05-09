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

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;

import java.util.List;

public class XmlPrettyPrinter {
    private final String xml;

    public XmlPrettyPrinter(String xml) {
        this.xml = xml;
    }

    public void prettyPrint(PrettyPrinter printer) {
        try {
            XmlNode node = XmlParser.parse(xml);
            printNode(printer, node);
        } catch (Throwable ignored) {
            printer.printLine(Color.RESET, xml);
        }
    }

    private void printNode(PrettyPrinter printer, XmlNode node) {
        if (node.isTextNode()) {
            printTextNode(printer, node);
        } else if (node.getChildren().isEmpty()) {
            printNoChildrenNode(printer, node);
        } else if (node.getChildren().size() == 1) {
            printNodeWithSingleChild(printer, node);
        } else if (node.getChildren().size() > 1) {
            printNodeWithMultipleChildren(printer, node);
        }
    }

    private void printNodeWithSingleChild(PrettyPrinter printer, XmlNode node) {
        printNodeStart(printer, node);
        printNode(printer, node.getChildren().get(0));
        printNodeEnd(printer, node);
    }

    private void printNodeWithMultipleChildren(PrettyPrinter printer, XmlNode node) {
        printNodeStart(printer, node);
        printer.increaseIndentation();
        printer.printLine();

        for (XmlNode child : node.getChildren()) {
            printNode(printer, child);
            printer.printLine();
        }

        printer.decreaseIndentation();
        printNodeEnd(printer, node);
    }

    private void printNoChildrenNode(PrettyPrinter printer, XmlNode node) {
        printer.printDelimiter("<");
        printer.print(Color.BLUE, node.getTagName());
        printAttributes(printer, node.getAttributes());
        printer.printDelimiter("/>");
    }

    private void printTextNode(PrettyPrinter printer, XmlNode node) {
        printer.print(FontStyle.RESET, node.getText());
    }

    private void printNodeStart(PrettyPrinter printer, XmlNode node) {
        printer.printDelimiter("<");
        printer.print(Color.BLUE, node.getTagName());
        printAttributes(printer, node.getAttributes());
        printer.printDelimiter(">");
    }

    private static void printNodeEnd(PrettyPrinter printer, XmlNode node) {
        printer.printDelimiter("</");
        printer.print(Color.BLUE, node.getTagName());
        printer.printDelimiter(">");
    }

    private void printAttributes(PrettyPrinter printer, List<XmlAttribute> attributes) {
        if (attributes.isEmpty()) {
            return;
        }
        
        printer.print(" ");
        int idx = 0;
        for (XmlAttribute attribute : attributes) {
            printAttribute(printer, attribute);

            boolean isLast = idx == attributes.size() - 1;
            if (!isLast) {
                printer.print(" ");
            }

            idx++;
        }
    }

    private void printAttribute(PrettyPrinter printer, XmlAttribute attribute) {
        printer.print(PrettyPrinter.KEY_COLOR, attribute.getName());
        printer.printDelimiter("=");
        printer.printObject(attribute.getValue());
    }
}
