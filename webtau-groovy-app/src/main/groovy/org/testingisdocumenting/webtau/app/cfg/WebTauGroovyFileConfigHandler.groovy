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

package org.testingisdocumenting.webtau.app.cfg

import org.testingisdocumenting.webtau.GroovyRunner
import org.testingisdocumenting.webtau.TestListener
import org.testingisdocumenting.webtau.TestListeners
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandler
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandlers
import org.testingisdocumenting.webtau.cfg.*
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.db.DbDataSourceProvider
import org.testingisdocumenting.webtau.db.DbDataSourceProviders
import org.testingisdocumenting.webtau.graphql.listener.GraphQLListener
import org.testingisdocumenting.webtau.graphql.listener.GraphQLListeners
import org.testingisdocumenting.webtau.http.listener.HttpListener
import org.testingisdocumenting.webtau.http.listener.HttpListeners
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandler
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.report.ReportGenerator
import org.testingisdocumenting.webtau.report.ReportGenerators
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean

class WebTauGroovyFileConfigHandler implements WebTauConfigHandler {
    private static final String SOURCE = 'config file'
    private static final String PERSONAS_KEY = 'personas'

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

        ConfigParserDslDelegate parserDslDelegate = parseConfig(cfg, existingConfig)

        if (!parserDslDelegate) {
            return
        }

        Map<String, ?> configAsMap = convertConfigToMap(existingConfig, cfg, parserDslDelegate)
        cfg.acceptConfigValues(SOURCE, configAsMap)

        handlePersonas(cfg, parserDslDelegate)

        setupHttpHeaderProvider(configAsMap)
        setupBrowserPageNavigationHandler(configAsMap)
        setupReportGenerator(configAsMap)
        setupPageElementGetSetValueHandlers(configAsMap)
        setupTestListeners(configAsMap)
        setupGraphQLListeners(configAsMap)
        setupHttpListeners(configAsMap)
        setupHttpValidationHandlers(configAsMap)
        setupDbDataSourceProviders(configAsMap)
    }

    private static void handlePersonas(WebTauConfig cfg, ConfigParserDslDelegate parserDslDelegate) {
        def envConfigValue = cfg.getEnvConfigValue()

        def valuesPerPersona = envConfigValue.isDefault() ?
                parserDslDelegate.@personaValues.valuesPerPersona:
                parserDslDelegate.@personaValues.valuesPerEnvPerPersona.get(envConfigValue.getAsString())

        if (valuesPerPersona) {
            valuesPerPersona.availablePersonas.each { personaId ->
                def configAsMap = valuesPerPersona.personaConfigAsMap(personaId)
                cfg.acceptConfigValues(SOURCE, personaId, configAsMap)
            }
        }
    }

    private static ConfigParserDslDelegate parseConfig(WebTauConfig cfg, Path configPath) {
        try {
            def groovy = GroovyRunner.createWithCustomScriptClass(cfg.workingDir, DelegatingScript.class)

            def configParserDslDelegate = new ConfigParserDslDelegate()
            DelegatingScript delegatingScript = groovy.createScript(configPath.toUri().toString(),
                    new GroovyConfigDslBinding(configParserDslDelegate))
            delegatingScript.setDelegate(configParserDslDelegate)
            delegatingScript.run()

            return configParserDslDelegate
        } catch (Exception e) {
            // main use case for this is during REPL mode
            // we don't want to crash the config class loading due to parsing error
            // because in REPL mode we should be able to recover and continue
            // otherwise we will have to restart REPL process
            if (isIgnoreConfigError()) {
                ConsoleOutputs.err(StackTraceUtils.fullCauseMessage(e))
                return null
            } else {
                throw e
            }
        }
    }

    private static Map<String, ?> convertConfigToMap(Path configPath,
                                                     WebTauConfig cfg,
                                                     ConfigParserDslDelegate parserDslDelegate) {
        def envConfigValue = cfg.getEnvConfigValue()
        if (envConfigValue.isDefault()) {
            return parserDslDelegate.toMap()
        }

        def env = envConfigValue.getAsString()
        if (!parserDslDelegate.getAvailableEnvironments().contains(env)) {
            throw new IllegalArgumentException("environment <$env> is not defined in " + configPath)
        }

        return parserDslDelegate.combinedValuesForEnv(env)
    }

    private static void setupHttpHeaderProvider(Map<String, ?> config) {
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

    private static void setupBrowserPageNavigationHandler(Map<String, ?> config) {
        def browserPageNavigationHandler = getClosure(config, 'browserPageNavigationHandler')
        if (browserPageNavigationHandler) {
            GroovyConfigBasedBrowserPageNavigationHandler.setHandler(browserPageNavigationHandler)
        }
    }

    private static void setupReportGenerator(Map<String, ?> config) {
        def generator = config.get('reportGenerator')
        def reportGenerator = generator ? generator as ReportGenerator : null
        if (reportGenerator) {
            ReportGenerators.add(reportGenerator)
        }
    }

    private static void setupPageElementGetSetValueHandlers(Map<String, ?> config) {
        List<PageElementGetSetValueHandler> handlerInstances = instancesFromConfig(config, 'pageElementGetSetValueHandlers')
        handlerInstances.each { PageElementGetSetValueHandlers.add(it) }
    }

    private static void setupTestListeners(Map<String, ?> config) {
        List<TestListener> listenerInstances = instancesFromConfig(config, 'testListeners')
        listenerInstances.each { TestListeners.add(it) }
    }

    private static void setupGraphQLListeners(Map<String, ?> config) {
        List<GraphQLListener> listenerInstances = instancesFromConfig(config, 'graphqlListeners')
        listenerInstances.each { GraphQLListeners.add(it) }
    }

    private static void setupHttpListeners(Map<String, ?> config) {
        List<HttpListener> listenerInstances = instancesFromConfig(config, 'httpListeners')
        listenerInstances.each { HttpListeners.add(it) }
    }

    private static void setupHttpValidationHandlers(Map<String, ?> config) {
        List<HttpValidationHandler> handlerInstances = instancesFromConfig(config, 'httpValidationHandlers')
        handlerInstances.each { HttpValidationHandlers.add(it) }
    }

    private static void setupDbDataSourceProviders(Map<String, ?> config) {
        List<DbDataSourceProvider> providers = instancesFromConfig(config, 'dbDataSourceProviders')
        providers.each { DbDataSourceProviders.add(it) }
    }

    private static <E> List<E> instancesFromConfig(Map<String, ?> config, String key) {
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

    private static Closure getClosure(Map<String, ?> config, String key) {
        return (Closure) config.get(key)
    }

    static boolean isIgnoreConfigError() {
        return System.hasProperty("ignoreGroovyConfigError")
    }

    static class GroovyConfigDslBinding extends Binding {
        private final ConfigParserDslDelegate parserDslDelegate

        GroovyConfigDslBinding(ConfigParserDslDelegate parserDslDelegate) {
            this.parserDslDelegate = parserDslDelegate
        }

        @Override
        Object getVariable(String name) {
            return parserDslDelegate.getProperty(name)
        }

        @Override
        void setVariable(String name, Object value) {
            parserDslDelegate.setProperty(name, value)
        }

        @Override
        void removeVariable(String name) {
            throw new UnsupportedOperationException("remove: $name")
        }

        @Override
        boolean hasVariable(String name) {
            return parserDslDelegate.getProperty(name) != null
        }

        @Override
        Map getVariables() {
            return parserDslDelegate.toMap()
        }
    }
}
