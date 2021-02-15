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

package org.testingisdocumenting.webtau.runner.standalone

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Path

class GroovyStandaloneEngine {
    static GroovyScriptEngine createWithoutDelegating(Path workingDir, List<String> staticImports) {
        return createImpl(workingDir, staticImports, null)
    }

    static GroovyScriptEngine createWithCustomScriptClass(Path workingDir, List<String> staticImports, Class scriptClass) {
        return createImpl(workingDir, staticImports, scriptClass)
    }

    private static GroovyScriptEngine createImpl(Path workingDir, List<String> staticImports, Class scriptClass) {
        def imports = new ImportCustomizer()
        staticImports.forEach { imports.addStaticStars(it) }

        def compilerCfg = new CompilerConfiguration()
        compilerCfg.addCompilationCustomizers(imports)

        if (scriptClass) {
            compilerCfg.scriptBaseClass = scriptClass.name
        }

        def engine = new GroovyScriptEngine(workingDir.toString())
        engine.config = compilerCfg

        return engine
    }
}