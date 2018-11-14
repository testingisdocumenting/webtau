/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.maven

import com.twosigma.webtau.cli.WebTauCliApp
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugin.logging.Log
import org.apache.maven.shared.model.fileset.FileSet
import org.apache.maven.shared.model.fileset.util.FileSetManager

class WebTauMaven {
    static void runTests(Log log, FileSet tests, Map options) {
        def fileSetManager = new FileSetManager()
        def files = fileSetManager.getIncludedFiles(tests) as List

        log.info("test files:\n    " + files.join("\n    "))

        def args = buildArgs(options)
        args.addAll(files)

        def cli = new WebTauCliApp(args as String[])
        if (options.interactive) {
            cli.startInteractive()
        } else {
            cli.start(WebTauCliApp.WebDriverBehavior.AutoCloseWebDrivers) { exitCode ->
                if (exitCode > 0) {
                    throw new MojoFailureException("tests failure: check tests output")
                }
            }
        }
    }

    private static List<String> buildArgs(Map params) {
        return params.entrySet()
                .findAll { e -> e.value != null }
                .collect { e -> argKeyValue(e.key, e.value) }
    }

    private static String argKeyValue(key, value) {
        if (value instanceof Boolean) {
            return value ? "--$key" : ""
        }

        return  "--${key}=${value}"
    }
}
