package com.twosigma.webtau.runner.standalone

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Path

class GroovyStandaloneEngine {
    static GroovyScriptEngine createWithDelegatingEnabled(Path workingDir, List<String> staticImports) {
        return createImpl(workingDir, staticImports, true)
    }

    static GroovyScriptEngine createWithoutDelegating(Path workingDir, List<String> staticImports) {
        return createImpl(workingDir, staticImports, false)
    }

    private static GroovyScriptEngine createImpl(Path workingDir, List<String> staticImports, boolean isDelegatingEnabled) {
        def imports = new ImportCustomizer()
        def fullListOfStatics = staticImports
        fullListOfStatics.forEach { imports.addStaticStars(it) }

        def compilerCfg = new CompilerConfiguration()
        compilerCfg.addCompilationCustomizers(imports)

        if (isDelegatingEnabled) {
            compilerCfg.scriptBaseClass = DelegatingScript.class.name
        }

        def engine = new GroovyScriptEngine(workingDir.toString())
        engine.config = compilerCfg

        return engine
    }
}