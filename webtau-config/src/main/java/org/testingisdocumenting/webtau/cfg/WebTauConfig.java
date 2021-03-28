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

package org.testingisdocumenting.webtau.cfg;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare;
import static org.testingisdocumenting.webtau.cfg.ConfigValue.declareBoolean;
import static org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation.DEFAULT_DOC_ARTIFACTS_DIR_NAME;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testingisdocumenting.webtau.console.ConsoleOutput;
import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.expectation.timer.SystemTimerConfig;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.StringUtils;
import org.testingisdocumenting.webtau.version.WebtauVersion;

public class WebTauConfig implements PrettyPrintable {
    private static final String SOURCE_MANUAL = "manual";

    public static final String CONFIG_FILE_DEPRECATED_DEFAULT = "webtau.cfg";
    public static final String CONFIG_FILE_NAME_DEFAULT = "webtau.cfg.groovy";

    private static final List<WebTauConfigHandler> handlers = discoverConfigHandlers();

    private static final Supplier<Object> NO_DEFAULT = () -> null;

    private final ConfigValue config = declare("config", "config file path", () -> CONFIG_FILE_NAME_DEFAULT);
    private final ConfigValue env = declare("env", "environment id", () -> "local");
    private final ConfigValue url = declare("url", "base url for application under test", NO_DEFAULT);

    private final ConfigValue verbosityLevel = declare("verbosityLevel", "output verbosity level. " +
            "0 - no output; 1 - test names; 2 - first level steps; etc", () -> Integer.MAX_VALUE);
    private final ConfigValue fullStackTrace = declare("fullStackTrace", "print full stack trace to console",
            () -> false);

    private final ConfigValue consolePayloadOutputLimit = declare("consolePayloadOutputLimit",
            "max number of lines to display in console for outputs (e.g. http response)", () -> 500);

    private final ConfigValue waitTimeout = declare("waitTimeout", "wait timeout in milliseconds", () -> SystemTimerConfig.DEFAULT_WAIT_TIMEOUT);

    private final ConfigValue httpTimeout = declare("httpTimeout", "http connect and read timeout in milliseconds", () -> 30000);

    private final ConfigValue disableFollowingRedirects = declareBoolean("disableRedirects", "disable following of redirects from HTTP calls", false);
    private final ConfigValue maxRedirects = declare("maxRedirects", "Maximum number of redirects to follow for an HTTP call", () -> 20);
    private final ConfigValue userAgent = declare("userAgent", "User agent to send on HTTP requests",
            () -> "webtau/" + WebtauVersion.getVersion());
    private final ConfigValue removeWebtauFromUserAgent = declare("removeWebtauFromUserAgent",
            "By default webtau appends webtau and its version to the user-agent, this disables that part",
            () -> false);
    private final ConfigValue workingDir = declare("workingDir", "logical working dir", () -> Paths.get(""));
    private final ConfigValue cachePath = declare("cachePath", "user driven cache base dir",
            () -> workingDir.getAsPath().resolve(".webtau-cache"));

    private final ConfigValue docPath = declare("docPath", "path for captured request/responses, screenshots and other generated " +
            "artifacts for documentation", () -> workingDir.getAsPath().resolve(DEFAULT_DOC_ARTIFACTS_DIR_NAME));
    private final ConfigValue noColor = declareBoolean("noColor", "disable ANSI colors", false);
    private final ConfigValue reportPath = declare("reportPath", "report file path", () -> getWorkingDir().resolve("webtau.report.html"));
    private final ConfigValue staleElementRetry = declare("staleElementRetry", "number of times to automatically retry for stale element actions", () -> 5);
    private final ConfigValue staleElementRetryWait = declare("staleElementRetryWait", "wait time in between stale element retries", () -> 100);

    private final Map<String, ConfigValue> enumeratedCfgValues = enumerateRegisteredConfigValues();

    private final List<ConfigValue> freeFormCfgValues = new ArrayList<>();

    private static final WebTauConfigHandler coreConfigHandler = new WebTauCoreConfigHandler();

    public static WebTauConfig getCfg() {
        return CfgInstanceHolder.INSTANCE;
    }

    /**
     * Handlers are automatically discovered using service loader.
     * Use this method to manually register additional config handler in front of the queue.
     * @param handler config handler to add
     */
    public static void registerConfigHandlerAsFirstHandler(WebTauConfigHandler handler) {
        handlers.add(0, handler);
    }

    public static void registerConfigHandlerAsLastHandler(WebTauConfigHandler handler) {
        handlers.add(handler);
    }

    public static void resetConfigHandlers() {
        handlers.clear();
        handlers.addAll(discoverConfigHandlers());
    }

    public void reset() {
        getEnumeratedCfgValuesStream().forEach(ConfigValue::reset);
        freeFormCfgValues.forEach(ConfigValue::reset);
    }

    protected WebTauConfig() {
        triggerConfigHandlers();
    }

    public void triggerConfigHandlers() {
        registeredHandlersAndCore().forEach(h -> h.onBeforeCreate(this));

        Map<String, ?> envVarValues = envVarsAsMap();
        acceptConfigValues("environment variable", envVarValues);
        acceptConfigValues("environment variable", convertWebTauEnvVarsToPropNames(envVarValues));

        acceptConfigValues("system property", systemPropsAsMap());

        registeredHandlersAndCore().forEach(h -> h.onAfterCreate(this));
    }

    public Stream<ConfigValue> getEnumeratedCfgValuesStream() {
        return enumeratedCfgValues.values().stream();
    }

    public ConfigValue findConfigValue(String key) {
        return enumeratedCfgValues.get(key);
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String key) {
        Stream<ConfigValue> allValues =
                Stream.concat(enumeratedCfgValues.values().stream(), freeFormCfgValues.stream());

        Optional<ConfigValue> configValue = allValues.filter(v -> v.match(key)).findFirst();
        return (E) configValue.map(ConfigValue::getAsObject).orElse(null);
    }

    public int getVerbosityLevel() {
        return verbosityLevel.getAsInt();
    }

    public boolean getFullStackTrace() {
        return fullStackTrace.getAsBoolean();
    }

    public int getConsolePayloadOutputLimit() {
        return consolePayloadOutputLimit.getAsInt();
    }

    public void acceptConfigValues(String source, Map<String, ?> values) {
        acceptConfigValues(source, Persona.DEFAULT_PERSONA_ID, values);
    }

    public void acceptConfigValues(String source, String personaId, Map<String, ?> values) {
        enumeratedCfgValues.values().forEach(v -> v.accept(source, personaId, values));

        registerFreeFormCfgValues(values);
        freeFormCfgValues.forEach(v -> v.accept(source, personaId, values));
    }

    // for REPL convenience
    public void setUrl(String url) {
        setBaseUrl(url);
    }

    public void setBaseUrl(String url) {
        this.url.set(SOURCE_MANUAL, url);
    }

    public String getBaseUrl() {
        return url.getAsString();
    }

    public String getEnv() {
        return env.getAsString();
    }

    public ConfigValue getEnvConfigValue() {
        return env;
    }

    public ConfigValue getConfigFileNameValue() {
        return config;
    }

    public ConfigValue getWorkingDirConfigValue() {
        return workingDir;
    }

    public ConfigValue getBaseUrlConfigValue() {
        return url;
    }

    public int getWaitTimeout() {
        return waitTimeout.getAsInt();
    }

    public int getHttpTimeout() {
        return httpTimeout.getAsInt();
    }

    public boolean shouldFollowRedirects() {
        return !disableFollowingRedirects.getAsBoolean();
    }

    public int maxRedirects() {
        return maxRedirects.getAsInt();
    }

    public String getUserAgent() {
        if (userAgent.isDefault()) {
            return userAgent.getAsString();
        }

        String finalUserAgent = userAgent.getAsString();
        if (!removeWebtauFromUserAgent.getAsBoolean()) {
            String defaultValue = userAgent.getDefaultValue().toString();
            finalUserAgent += " (" + defaultValue + ")";
        }

        return finalUserAgent;
    }

    public ConfigValue getUserAgentConfigValue() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent.set(SOURCE_MANUAL, userAgent);
    }

    public void setRemoveWebtauFromUserAgent(boolean remove) {
        this.removeWebtauFromUserAgent.set(SOURCE_MANUAL, remove);
    }

    public ConfigValue getRemoveWebtauFromUserAgentConfigValue() {
        return removeWebtauFromUserAgent;
    }

    public ConfigValue getDocArtifactsPathConfigValue() {
        return docPath;
    }

    public Path getDocArtifactsPath() {
        return getWorkingDir().resolve(docPath.getAsPath());
    }

    public boolean isAnsiEnabled() {
        return !noColor.getAsBoolean();
    }

    public int getStaleElementRetry() {
        return staleElementRetry.getAsInt();
    }

    public int getStaleElementRetryWait() {
        return staleElementRetryWait.getAsInt();
    }

    public Path getWorkingDir() {
        return workingDir.getAsPath();
    }

    public Path fullPath(String relativeOrFull) {
        return fullPath(Paths.get(relativeOrFull));
    }

    public Path fullPath(Path relativeOrFull) {
        if (relativeOrFull == null) {
            return null;
        }

        if (relativeOrFull.isAbsolute()) {
            return relativeOrFull;
        }

        return getWorkingDir().resolve(relativeOrFull).toAbsolutePath();
    }

    public Path getCachePath() {
        return cachePath.getAsPath();
    }

    public Path getReportPath() {
        return reportPath.getAsPath();
    }

    public ConfigValue getReportPathConfigValue() {
        return reportPath;
    }

    public String getWorkingDirConfigName() {
        return workingDir.getKey();
    }

    @Override
    public String toString() {
        return Stream.concat(enumeratedCfgValues.values().stream(), freeFormCfgValues.stream())
                .map(ConfigValue::toString)
                .collect(Collectors.joining("\n"));
    }

    public void printEnumerated() {
        printConfig(ConsoleOutputs.asCombinedConsoleOutput(), enumeratedCfgValues.values());
    }

    private void printConfig(ConsoleOutput console, Collection<ConfigValue> configValues) {
        int maxKeyLength = configValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getKey().length()).max(Integer::compareTo).orElse(0);

        int maxValueLength = configValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getAsString().length()).max(Integer::compareTo).orElse(0);

        configValues.stream().filter(ConfigValue::nonDefault).forEach(v -> {
            String valueAsText = v.getAsString();
            int valuePadding = maxValueLength - valueAsText.length();

            console.out(Color.BLUE, String.format("%" + maxKeyLength + "s", v.getKey()), ": ",
                    Color.YELLOW, valueAsText,
                    StringUtils.createIndentation(valuePadding),
                    FontStyle.NORMAL, " // from ", v.getSource());
                }
        );
    }

    private Stream<WebTauConfigHandler> registeredHandlersAndCore() {
        return Stream.concat(handlers.stream(), Stream.of(coreConfigHandler));
    }

    private void registerFreeFormCfgValues(Map<String, ?> values) {
        Stream<String> keys = values.keySet().stream()
                .filter(k -> noConfigValuePresent(enumeratedCfgValues.values(), k));

        keys.filter(k -> noConfigValuePresent(freeFormCfgValues, k))
                .forEach(k -> {
                    ConfigValue configValue = declare(k, "free form cfg value", () -> null);
                    freeFormCfgValues.add(configValue);
                });
    }

    private boolean noConfigValuePresent(Collection<ConfigValue> configValues, String key) {
        return configValues.stream().noneMatch(cv -> cv.match(key));
    }

    private static Map<String, ?> systemPropsAsMap() {
        return System.getProperties().stringPropertyNames().stream()
                .collect(Collectors.toMap(n -> n, System::getProperty));
    }

    private static Map<String, ?> envVarsAsMap() {
        return System.getenv();
    }

    private Map<String, ?> convertWebTauEnvVarsToPropNames(Map<String, ?> envVarValues) {
        Map<String, String> result = new LinkedHashMap<>();
        envVarValues.forEach((k, v) -> {
            if (k.startsWith(ConfigValue.ENV_VAR_PREFIX)) {
                result.put(convertToCamelCase(k), v.toString());
            }
        });

        return result;
    }

    static String convertToCamelCase(String key) {
        String[] parts = key.split("_");
        String joined = Arrays.stream(parts)
                .skip(1)
                .map(p -> p.charAt(0) + p.substring(1).toLowerCase())
                .collect(Collectors.joining(""));

        return Character.toLowerCase(joined.charAt(0)) + joined.substring(1);
    }

    private Map<String, ConfigValue> enumerateRegisteredConfigValues() {
        Stream<ConfigValue> standardConfigValues = Stream.of(
                config,
                env,
                url,
                verbosityLevel,
                fullStackTrace,
                workingDir,
                waitTimeout,
                httpTimeout,
                disableFollowingRedirects,
                maxRedirects,
                userAgent,
                removeWebtauFromUserAgent,
                docPath,
                reportPath,
                noColor,
                consolePayloadOutputLimit,
                staleElementRetry,
                staleElementRetryWait,
                cachePath);

        Stream<ConfigValue> additionalConfigValues = handlers.stream()
                .flatMap(WebTauConfigHandler::additionalConfigValues);

        return Stream.concat(standardConfigValues, additionalConfigValues)
                .collect(Collectors.toMap(ConfigValue::getKey, v -> v, (o, n) -> n, LinkedHashMap::new));
    }

    @Override
    public void prettyPrint(ConsoleOutput console) {
        printConfig(console, freeFormCfgValues);
        printConfig(console, enumeratedCfgValues.values());
    }

    private static class CfgInstanceHolder {
        private static final WebTauConfig INSTANCE = new WebTauConfig();
    }

    private static List<WebTauConfigHandler> discoverConfigHandlers() {
        return ServiceLoaderUtils.load(WebTauConfigHandler.class);
    }
}
