/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cfg

import org.testingisdocumenting.webtau.browser.page.value.handlers.PageElementGetSetValueHandler
import org.testingisdocumenting.webtau.browser.page.value.handlers.PageElementGetSetValueHandlers
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.db.DbDataSourceProvider
import org.testingisdocumenting.webtau.db.DbDataSourceProviders
import org.testingisdocumenting.webtau.report.ReportGenerator
import org.testingisdocumenting.webtau.report.ReportGenerators
import org.testingisdocumenting.webtau.reporter.TestListener
import org.testingisdocumenting.webtau.reporter.TestListeners
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean

class WebTauGroovyFileConfigHandler implements WebTauConfigHandler {
    private static final AtomicBoolean ignoreConfigErrors = new AtomicBoolean(false)

    static void forceIgnoreErrors() {
        ignoreConfigErrors.set(true)
    }

    @Override
    void onBeforeCreate(WebTauConfig cfg) {
    }

    @Override
    void onAfterCreate(WebTauConfig cfg) {
        Path workingDir = cfg.workingDir.toAbsolutePath()

        Path configPath = workingDir.resolve(cfg.configFileNameValue.asString)
        Path deprecatedConfigPath = workingDir.resolve(WebTauConfig.CONFIG_FILE_DEPRECATED_DEFAULT)

        Path existingConfig = [configPath, deprecatedConfigPath].find { Files.exists(it) }

        if (!existingConfig) {
            ConsoleOutputs.out('skipping config file as it is not found: ', configPath)
            return
        }

        validateEnv(cfg, workingDir, existingConfig)

        ConfigObject parsedConfig = parseConfig(cfg, existingConfig)

        if (!parsedConfig) {
            return
        }

        cfg.acceptConfigValues("config file", convertConfigToMap(parsedConfig))

        setupHttpHeaderProvider(parsedConfig)
        setupBrowserPageNavigationHandler(parsedConfig)
        setupReportGenerator(parsedConfig)
        setupPageElementGetSetValueHandlers(parsedConfig)
        setupTestListeners(parsedConfig)
        setupDbDataSourceProviders(parsedConfig)
    }

    private static ConfigObject parseConfig(WebTauConfig cfg, Path configPath) {
        try {
            def groovy = GroovyRunner.createWithoutDelegating(cfg.workingDir)

            ConfigSlurper configSlurper = new ConfigSlurper(cfg.env)
            def configScript = groovy.createScript(configPath.toUri().toString(), new ConfigBinding())

            def parsedConfig = configSlurper.parse(configScript)
            return parsedConfig
        } catch (Exception e) {
            // main use case for this is during REPL mode
            // we don't want to crash the config class loading due to parsing error
            // because in REPL mode we should be able to recover and continue
            // otherwise we will have to restart REPL process
            if (ignoreConfigErrors.get()) {
                ConsoleOutputs.err(StackTraceUtils.fullCauseMessage(e))
                return null
            } else {
                throw e
            }
        }
    }

    private static Map<String, ?> convertConfigToMap(ConfigObject configObject) {
        Map result = new LinkedHashMap()
        configObject.each { k, v ->
            result[k] = v instanceof ConfigObject ?
                    convertConfigToMap(v) :
                    v
        }

        return result
    }

    private static void setupHttpHeaderProvider(ConfigObject config) {
        def headerProvider = getClosure(config, 'httpHeaderProvider')
        if (headerProvider) {
            // we cannot add configuration here using HttpConfigurations.add since most likely config setup will be triggered
            // as part of the first cfg value access (e.g. baseUrl lookup).
            // to lookup base url webtau loops through registered handlers, adding new handlers will cause
            // loop exception.
            // so we register GroovyConfigBasedHttpConfiguration via service loaded,
            // and adding actual header provider now
            GroovyConfigBasedHttpConfiguration.setHeaderProvider(headerProvider)
        }
    }

    private static void setupBrowserPageNavigationHandler(ConfigObject config) {
        def browserPageNavigationHandler = getClosure(config, 'browserPageNavigationHandler')
        if (browserPageNavigationHandler) {
            GroovyConfigBasedBrowserPageNavigationHandler.setHandler(browserPageNavigationHandler)
        }
    }

    private static void setupReportGenerator(ConfigObject config) {
        def generator = config.get('reportGenerator')
        def reportGenerator = generator ? generator as ReportGenerator : null
        if (reportGenerator) {
            ReportGenerators.add(reportGenerator)
        }
    }

    private static void setupPageElementGetSetValueHandlers(ConfigObject config) {
        List<PageElementGetSetValueHandler> handlerInstances = instancesFromConfig(config, 'pageElementGetSetValueHandlers')
        handlerInstances.each { PageElementGetSetValueHandlers.add(it) }
    }

    private static void setupTestListeners(ConfigObject config) {
        List<TestListener> listenerInstances = instancesFromConfig(config, 'testListeners')
        listenerInstances.each { TestListeners.add(it) }
    }

    private static void setupDbDataSourceProviders(ConfigObject config) {
        List<DbDataSourceProvider> providers = instancesFromConfig(config, 'dbDataSourceProviders')
        providers.each { DbDataSourceProviders.add(it) }
    }

    private static <E> List<E> instancesFromConfig(ConfigObject config, String key) {
        def classes = (List<Class<E>>) config.get(key)
        if (!classes) {
            return []
        }

        return classes.collect{ c -> (E) constructFromClass(c) }
    }

    private static Object constructFromClass(Class handlerClass) {
        def defaultConstructor = handlerClass.constructors.find { constructor -> constructor.parameterCount == 0 }
        if (!defaultConstructor) {
            throw new IllegalArgumentException("${handlerClass} must have default constructor")
        }
        return defaultConstructor.newInstance()
    }

    private static Closure getClosure(ConfigObject config, String key) {
        return (Closure) config.get(key)
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
}
