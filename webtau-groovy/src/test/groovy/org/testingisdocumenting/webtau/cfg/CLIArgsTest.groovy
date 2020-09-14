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

package org.testingisdocumenting.webtau.cfg

import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation
import org.testingisdocumenting.webtau.utils.FileUtils
import org.testingisdocumenting.webtau.utils.JsonUtils
import org.junit.Test

import java.util.stream.Collectors

class CLIArgsTest {
    @Test
    void "generate CLI docs"() {
        try {
            def handler = new WebTauGroovyCliArgsConfigHandler('--workingDir=${workingDir}', 'test.groovy')

            WebTauConfig.registerConfigHandlerAsFirstHandler(handler)

            def cfg = new WebTauConfig()

            def cfgValues = cfg.getEnumeratedCfgValuesStream()
            def cfgList = cfgValues.map { cfgValue ->
                def defaultValue = getDefaultValue(cfgValue)
                return [
                        name                  : cfgValue.key,
                        'environment variable': cfgValue.prefixedUpperCaseKey,
                        description           : cfgValue.description,
                        'default value'       : defaultValue,
                ]
            }
            .sorted { a, b ->
                return a.name <=> b.name
            }
            .collect(Collectors.toList())

            cfgList.stream().anyMatch {
                it.name == "numberOfThreads"
            }.should == true

            cfgList.forEach {
                if (it.name == 'workingDir') {
                    it.'default value'.should == ""
                } else if (it.name == 'docPath') {
                    it.'default value'.should == '${workingDir}' + File.separator + 'doc-artifacts'
                }
            }

            def artifactPath = DocumentationArtifactsLocation.classBasedLocation(CLIArgsTest)
                    .resolve('doc-artifacts/cfg/cli-args.json')

            FileUtils.writeTextContent(artifactPath, JsonUtils.serializePrettyPrint(cfgList))
        } finally {
            WebTauConfig.resetConfigHandlers()
        }
    }

    private static String getDefaultValue(ConfigValue cfgValue) {
        return cfgValue.key == 'workingDir' ? "" : (cfgValue.getAsString() ?: "")
    }
}
