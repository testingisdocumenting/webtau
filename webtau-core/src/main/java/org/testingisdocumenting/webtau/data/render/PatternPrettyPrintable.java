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

package org.testingisdocumenting.webtau.data.render;

import org.testingisdocumenting.webtau.console.ansi.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternPrettyPrintable implements PrettyPrintable {
    private final Pattern pattern;

    public PatternPrettyPrintable(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printDelimiter("~/");
        printer.print(PrettyPrinter.STRING_COLOR, pattern.pattern());
        printer.printDelimiter("/");

        if (pattern.flags() > 0) {
            printer.printDelimiter(" (");
            printer.print(Color.PURPLE, String.join(" ", flagsToText()));
            printer.printDelimiter(")");
        }
    }

    private List<String> flagsToText() {
        List<String> result = new ArrayList<>();
        if ((pattern.flags() & Pattern.UNIX_LINES) != 0) {
            result.add("UNIX_LINES");
        }

        if ((pattern.flags() & Pattern.CASE_INSENSITIVE) != 0) {
            result.add("CASE_INSENSITIVE");
        }

        if ((pattern.flags() & Pattern.COMMENTS) != 0) {
            result.add("COMMENTS");
        }

        if ((pattern.flags() & Pattern.MULTILINE) != 0) {
            result.add("MULTILINE");
        }

        if ((pattern.flags() & Pattern.LITERAL) != 0) {
            result.add("LITERAL");
        }

        if ((pattern.flags() & Pattern.DOTALL) != 0) {
            result.add("DOTALL");
        }

        if ((pattern.flags() & Pattern.UNICODE_CASE) != 0) {
            result.add("UNICODE_CASE");
        }

        if ((pattern.flags() & Pattern.CANON_EQ) != 0) {
            result.add("CANON_EQ");
        }

        if ((pattern.flags() & Pattern.UNICODE_CHARACTER_CLASS) != 0) {
            result.add("UNICODE_CHARACTER_CLASS");
        }

        return result;
    }
}
