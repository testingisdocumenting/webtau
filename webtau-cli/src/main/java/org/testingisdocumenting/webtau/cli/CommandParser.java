/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.cli;

import java.util.ArrayList;
import java.util.List;

class CommandParser {
    private final String command;
    private final List<String> parts;

    private final StringBuilder current;

    static String[] splitCommand(String command) {
        return new CommandParser(command).parse();
    }

    private CommandParser(String command) {
        this.command = command;
        this.current = new StringBuilder();
        this.parts = new ArrayList<>();
    }

    public String[] parse() {
        boolean insideQuote = false;

        char previousChar = ' ';
        for (int idx = 0; idx < command.length(); idx++) {
            char c = command.charAt(idx);
            if (c == '"' && previousChar != '\\') {
                insideQuote = !insideQuote;
            }

            if (c == ' ' && !insideQuote) {
                flush();
            }

            current.append(c);
            previousChar = c;
        }

        flush();

        return parts.toArray(new String[0]);
    }

    private void flush() {
        String currentPart = current.toString().trim();
        if (currentPart.isEmpty()) {
            return;
        }

        parts.add(currentPart);
        current.setLength(0);
    }
}
