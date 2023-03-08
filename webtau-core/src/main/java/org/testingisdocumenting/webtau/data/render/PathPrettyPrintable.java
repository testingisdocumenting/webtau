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
import org.testingisdocumenting.webtau.utils.FileUtils;

import java.nio.file.Path;

public class PathPrettyPrintable implements PrettyPrintable {
    private final Path path;

    public PathPrettyPrintable(Path path) {
        this.path = path;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        if (path.isAbsolute()) {
            printer.printDelimiter("/");
        }

        if (path.getParent() != null) {
            for (Path part : path.getParent()) {
                printer.print(Color.RESET, part);
                printer.printDelimiter("/");
            }
        }

        String nameWithoutExtension = FileUtils.extractNameWithoutExtension(path);
        String extension = FileUtils.extractExtension(path);

        printer.print(Color.PURPLE, nameWithoutExtension);
        if (!extension.isEmpty()) {
            printer.print(Color.BLUE, ".", extension);
        }
    }
}
