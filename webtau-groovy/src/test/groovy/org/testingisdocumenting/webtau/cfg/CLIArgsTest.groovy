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
                    it.'default value'.should == '${workingDir}/doc-artifacts'
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
