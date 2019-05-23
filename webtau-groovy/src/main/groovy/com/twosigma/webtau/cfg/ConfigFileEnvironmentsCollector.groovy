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

package com.twosigma.webtau.cfg

import groovy.transform.PackageScope

import java.nio.file.Path

@PackageScope
class ConfigFileEnvironmentsCollector {
    private final Path configPath
    private final GroovyScriptEngine groovy
    private final List<String> environments
    private final Path workingDir

    ConfigFileEnvironmentsCollector(Path workingDir, Path configPath) {
        this.workingDir = workingDir
        this.configPath = configPath
        this.groovy = GroovyRunner.createWithCustomScriptClass(workingDir, IgnoreAllConfigScriptClass.class)
        this.environments = ['local']
    }

    List<String> collectEnvironments() {
        def script = groovy.createScript(configPath.toUri().toString(),
                [environments: this.&environmentsHandler] as Binding)
        script.run()

        return environments
    }

    def methodMissing(String name, def args) {
        environments.add(name)
    }

    private void environmentsHandler(Closure code) {
        code.delegate = this
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}

