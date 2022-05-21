/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.app.cfg

import org.junit.Test
import org.testingisdocumenting.webtau.cfg.ConfigValue
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.utils.FileUtils
import org.testingisdocumenting.webtau.utils.JsonUtils

import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class CLIArgsTest {
    @Test
    void "generate and validate CLI docs"() {
        try {
            def handler = new WebTauGroovyCliArgsConfigHandler('--workingDir=${workingDir}', 'test.groovy')

            WebTauConfig.registerConfigHandlerAsFirstHandler(handler)

            def cfg = new WebTauConfig()

            def cfgValues = cfg.getEnumeratedCfgValuesStream()
            def configPropsList = cfgValues.map { cfgValue ->
                def defaultValue = getDefaultValue(cfgValue)
                return [
                        name: cfgValue.key,
                        defaultValue: defaultValue,
                        description: cfgValue.description + generateDefaultValueMarkdown(defaultValue),
                        envVar: cfgValue.prefixedUpperCaseKey,
                        type: "",
                ]
            }
            .sorted { a, b ->
                return a.name <=> b.name
            }
            .collect(Collectors.toList())

            configPropsList.stream().anyMatch {
                it.name == "numberOfThreads"
            }.should == true

            configPropsList.forEach {
                if (it.name == 'workingDir') {
                    it.defaultValue.should == "<current-dir>"
                } else if (it.name == 'docPath') {
                    it.defaultValue.should == '${workingDir}' + File.separator + 'doc-artifacts'
                }
            }

            def envVarsList = configPropsList.collect {
                [
                        name: it.envVar,
                        description: it.description,
                        type: "",
                ]
            }

            // not using doc.captureJson as we modified <workingDir> to have predictable value for documentation
            FileUtils.writeTextContent(Paths.get('doc-artifacts/cfg/props.json'),
                    JsonUtils.serializePrettyPrint(configPropsList))
            FileUtils.writeTextContent(Paths.get('doc-artifacts/cfg/envVars.json'),
                    JsonUtils.serializePrettyPrint(envVarsList))
        } finally {
            WebTauConfig.resetConfigHandlers()
        }
    }

    private static String generateDefaultValueMarkdown(String v) {
        if (v.isEmpty()) {
            return ""
        }

        return "\n\n*default value*: $v"
    }

    private static String getDefaultValue(ConfigValue cfgValue) {
        if (cfgValue.key == 'workingDir') {
            return "<current-dir>"
        }

        def defaultV = cfgValue.defaultValue
        if (defaultV == null) {
            return ""
        }

        if (defaultV instanceof Path) {
            def idxOfWorkingDir = defaultV.toString().indexOf('${workingDir}')
            if (idxOfWorkingDir == -1) {
                return defaultV.toString()
            }

            return defaultV.toString().substring(idxOfWorkingDir)
        }

        return cfgValue.getAsString()
    }
}
