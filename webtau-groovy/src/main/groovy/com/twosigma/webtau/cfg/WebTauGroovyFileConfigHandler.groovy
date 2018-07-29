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

package com.twosigma.webtau.cfg

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.report.ReportGenerator
import com.twosigma.webtau.report.ReportGenerators

import java.nio.file.Files
import java.nio.file.Path

class WebTauGroovyFileConfigHandler implements WebTauConfigHandler {
    @Override
    void onBeforeCreate(WebTauConfig cfg) {
    }

    @Override
    void onAfterCreate(WebTauConfig cfg) {
        Path workingDir = cfg.workingDir.toAbsolutePath()
        Path configPath = workingDir.resolve(cfg.configFileName.asString)

        if (!Files.exists(configPath)) {
            ConsoleOutputs.out("skipping config file as it is not found: ", configPath)
            return
        }

        validateEnv(cfg, workingDir, configPath)

        def groovy = GroovyRunner.createWithoutDelegating(cfg.workingDir)

        ConfigSlurper configSlurper = new ConfigSlurper(cfg.env)
        def configScript = groovy.createScript(configPath.toString(), new ConfigBinding())

        def parsedConfig = configSlurper.parse(configScript)
        cfg.acceptConfigValues("config file", parsedConfig.flatten())

        if (!parsedConfig) {
            return
        }

        def headerProvider = httpHeaderProvider(parsedConfig)
        if (headerProvider) {
            // we cannot add configuration here using HttpConfigurations.add since most likely config setup will be triggered
            // as part of the first cfg value access (e.g. baseUrl lookup).
            // to lookup base url webtau loops through registered handlers, adding new handlers will cause
            // loop exception.
            // so we register GroovyConfigBasedHttpConfiguration via service loaded,
            // and adding actual header provider now
            GroovyConfigBasedHttpConfiguration.setHeaderProvider(headerProvider)
        }

        def reportGenerator = reportGenerator(parsedConfig)
        if (reportGenerator) {
            ReportGenerators.add(reportGenerator)
        }
    }

    private static void validateEnv(WebTauConfig cfg, Path workingDir, Path configPath) {
        def envConfigValue = cfg.getEnvConfigValue()
        if (envConfigValue.isDefault()) {
            return
        }

        def collector = new ConfigFileEnvironmentsCollector(workingDir, configPath)
        def definedEnvs = collector.collectEnvironments()

        def env = cfg.getEnv()
        if (!definedEnvs.contains(env)) {
            throw new IllegalArgumentException("environment <$env> is not defined in " + configPath)
        }
    }

    private static Closure httpHeaderProvider(parsedConfig) {
        def provider = parsedConfig.get("httpHeaderProvider")
        return provider ? provider as Closure : null
    }

    private static ReportGenerator reportGenerator(parsedConfig) {
        def generator = parsedConfig.get("reportGenerator")
        return generator ? generator as ReportGenerator : null
    }
}
