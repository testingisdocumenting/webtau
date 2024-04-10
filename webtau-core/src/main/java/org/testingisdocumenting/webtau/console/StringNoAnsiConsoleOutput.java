/*
 * Copyright 2024 webtau maintainers
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

package org.testingisdocumenting.webtau.console;

import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString;

import java.util.ArrayList;
import java.util.List;

public class StringNoAnsiConsoleOutput implements ConsoleOutput {
    private final List<String> out = new ArrayList<>();
    private final List<String> err = new ArrayList<>();

    @Override
    public void out(Object... styleOrValues) {
        out.add(new IgnoreAnsiString(styleOrValues).toString());
    }

    @Override
    public void err(Object... styleOrValues) {
        err.add(new IgnoreAnsiString(styleOrValues).toString());
    }

    public String getOut() {
        return String.join("\n", out);
    }

    public String getErr() {
        return String.join("\n", err);
    }
}
