/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.examples

import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.utils.FileUtils
import com.twosigma.webtau.utils.ServiceLoaderUtils

import java.nio.file.Path

import static com.twosigma.webtau.console.ConsoleOutputs.out

class ExamplesScaffolder {
    private static List<ExamplesProvider> providers = discoverProviders()
    public static final String EXAMPLES_DIR_NAME = "examples"

    private ExamplesScaffolder() {
    }

    static void scaffold(Path root) {
        providers
                .collectMany { it.provide() }
                .each {
                    scaffoldExample(root, it)
                }
    }

    private static void scaffoldExample(Path root, Example example) {
        def dir = root.resolve(EXAMPLES_DIR_NAME).resolve(example.dirName)

        out("generating example: ", Color.GREEN, EXAMPLES_DIR_NAME, "/", example.dirName,
                Color.PURPLE, " (cd ", dir.toAbsolutePath(), " && webtau ${example.fileName})")

        FileUtils.writeTextContent(dir.resolve(example.fileName), example.exampleBody)
        FileUtils.writeTextContent(dir.resolve(WebTauConfig.CONFIG_FILE_NAME_DEFAULT), example.configBody)
    }

    private static List<ExamplesProvider> discoverProviders() {
        def providers = ServiceLoaderUtils.load(ExamplesProvider)
        return providers.empty ? Collections.singletonList(new DefaultExamplesProvider()) : providers
    }
}
