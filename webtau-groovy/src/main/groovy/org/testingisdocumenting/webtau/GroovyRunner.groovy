/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau

import org.testingisdocumenting.webtau.runner.standalone.GroovyStandaloneEngine

import java.nio.file.Path

class GroovyRunner {
    private static final List<String> staticImports = ["org.testingisdocumenting.webtau.WebTauGroovyDsl"]

    static GroovyScriptEngine createWithoutDelegating(Path workingDir) {
        return GroovyStandaloneEngine.createWithoutDelegating(workingDir, staticImports)
    }

    static GroovyScriptEngine createWithCustomScriptClass(Path workingDir, Class scriptClass) {
        return GroovyStandaloneEngine.createWithCustomScriptClass(workingDir, staticImports, scriptClass)
    }
}
