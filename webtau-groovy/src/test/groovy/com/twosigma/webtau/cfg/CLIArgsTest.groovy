package com.twosigma.webtau.cfg

import com.twosigma.webtau.utils.FileUtils
import groovy.json.JsonOutput
import org.junit.Test

import java.nio.file.Paths
import java.util.stream.Collectors

class CLIArgsTest {
    @Test
    void "generate CLI docs"() {
        try {
            def handler = new WebTauGroovyCliArgsConfigHandler('--workingDir=${workingDir}', 'test.groovy')

            WebTauConfig.registerConfigHandlerAsFirstHandler(handler)

            def cfg = new WebTauConfig()

            def cfgValues = cfg.getCfgValuesStream()
            def cfgList = cfgValues.map { cfgValue ->
                def defaultValue = cfgValue.key == 'workingDir' ? "" : (cfgValue.getAsString() ?: "")
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
                    it.'default value'.should == '${workingDir}'
                }
            }

            def artifactPath = Paths.get('doc-artifacts/cfg/cli-args.json')
            FileUtils.writeTextContent(artifactPath, JsonOutput.toJson(cfgList))
        } finally {
            WebTauConfig.resetConfigHandlers()
        }
    }
}
