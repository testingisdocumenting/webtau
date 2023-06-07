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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.console.ansi.AnsiAsStylesValuesListConsoleOutput;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Map;

import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.TokenTypes.*;

/**
 * we convert tokens to a map so Web report can render them in its own way.
 * PrettyPrintable values already have extensive rendering logic, and instead of replicating it on Web,
 * we will render it in memory and pass ansi sequences to the report to render as is.
 */
class MessageTokenToMapConverter {
    private MessageTokenToMapConverter() {
    }

    static Map<String, Object> convert(MessageToken token) {
        if (token.type().equals(PRETTY_PRINT_VALUE.getType()) || token.type().equals(PRETTY_PRINT_VALUE_FIRST_LINES.getType())) {
            return convertPrettyPrintToMap(token);
        } else {
            return CollectionUtils.map("type", token.type(), "value", token.value());
        }
    }

    private static Map<String, Object> convertPrettyPrintToMap(MessageToken token) {
        PrettyPrinter printer = new PrettyPrinter(0);
        printer.printObject(token.value());
        printer.flushCurrentLine();

        AnsiAsStylesValuesListConsoleOutput ansiStylesConsoleOutput = new AnsiAsStylesValuesListConsoleOutput();
        printer.renderToConsole(ansiStylesConsoleOutput);

        return CollectionUtils.map("type", "styledText", "value", ansiStylesConsoleOutput.toListOfListsOfMaps());
    }
}
