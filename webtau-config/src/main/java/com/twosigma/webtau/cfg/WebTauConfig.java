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

package com.twosigma.webtau.cfg;

import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.console.ansi.FontStyle;
import com.twosigma.webtau.utils.ServiceLoaderUtils;
import com.twosigma.webtau.utils.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.webtau.cfg.ConfigValue.declare;
import static com.twosigma.webtau.cfg.ConfigValue.declareBoolean;

public class WebTauConfig {
    public static String CONFIG_FILE_NAME_DEFAULT = "webtau.cfg";

    private static final List<WebTauConfigHandler> handlers = discoverConfigHandlers();

    private static final Supplier<Object> NO_DEFAULT = () -> null;

    private final ConfigValue config = declare("config", "config file path", () -> CONFIG_FILE_NAME_DEFAULT);
    private final ConfigValue interactive = declareBoolean("interactive", "use CLI interactive mode");
    private final ConfigValue env = declare("env", "environment id", () -> "local");
    private final ConfigValue url = declare("url", "base url for application under test", NO_DEFAULT);
    private final ConfigValue verbosityLevel = declare("verbosityLevel", "output verbosity level. " +
            "0 - no output; 1 - test names; 2 - first level steps; etc", () -> Integer.MAX_VALUE);
    private final ConfigValue waitTimeout = declare("waitTimeout", "wait timeout in milliseconds", () -> 5000);
    private final ConfigValue disableFollowingRedirects = declareBoolean("disableRedirects", "disable following of redirects from HTTP calls");
    private final ConfigValue maxRedirects = declare("maxRedirects", "Maximum number of redirects to follow for an HTTP call", () -> 20);
    private final ConfigValue userAgent = declare("userAgent", "User agent to send on HTTP requests",
            () -> "webtau/" + WebTauMeta.getVersion());
    private final ConfigValue removeWebtauFromUserAgent = declare("removeWebtauFromUserAgent",
            "By default webtau appends webtau and its version to the user-agent, this disables that part",
            () -> false);
    private final ConfigValue workingDir = declare("workingDir", "logical working dir", () -> Paths.get(""));
    private final ConfigValue cachePath = declare("cachePath", "user driven cache file path",
            () -> workingDir.getAsPath().resolve(".webtau.cache.json"));

    private final ConfigValue docPath = declare("docPath", "path for captured request/responses, screenshots and other generated " +
            "artifacts for documentation", workingDir::getAsPath);
    private final ConfigValue noColor = declareBoolean("noColor", "disable ANSI colors");
    private final ConfigValue reportPath = declare("reportPath", "report file path", () -> getWorkingDir().resolve("webtau.report.html"));
    private final ConfigValue windowWidth = declare("windowWidth", "browser window width", () -> 1000);
    private final ConfigValue windowHeight = declare("windowHeight", "browser window height", () -> 800);
    private final ConfigValue staleElementRetry = declare("staleElementRetry", "number of times to automatically retry for stale element actions", () -> 5);
    private final ConfigValue staleElementRetryWait = declare("staleElementRetryWait", "wait time in between stale element retries", () -> 100);
    private final ConfigValue headless = declareBoolean("headless", "run headless mode");
    private final ConfigValue chromeDriverPath = declare("chromeDriverPath", "path to chrome driver binary", NO_DEFAULT);
    private final ConfigValue chromeBinPath = declare("chromeBinPath", "path to chrome binary", NO_DEFAULT);
    private final ConfigValue envPath = declare("envPath", "path items to append to path used for cli tests", Collections::emptyList);

    private final Map<String, ConfigValue> enumeratedCfgValues = enumerateRegisteredConfigValues();

    private final List<ConfigValue> freeFormCfgValues = new ArrayList<>();

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
        getCfgValuesStream().forEach(ConfigValue::reset);
    }

    protected WebTauConfig() {
        triggerConfigHandlers();
    }

    public void triggerConfigHandlers() {
        handlers.forEach(h -> h.onBeforeCreate(this));

        Map<String, ?> envVarValues = envVarsAsMap();
        acceptConfigValues("environment variable", envVarValues);
        acceptConfigValues("environment variable", convertWebTauEnvVarsToPropNames(envVarValues));

        acceptConfigValues("system property", systemPropsAsMap());

        handlers.forEach(h -> h.onAfterCreate(this));
    }

    public Stream<ConfigValue> getCfgValuesStream() {
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

    public void acceptConfigValues(String source, Map<String, ?> values) {
        enumeratedCfgValues.values().forEach(v -> v.accept(source, values));

        registerFreeFormCfgValues(values);
        freeFormCfgValues.forEach(v -> v.accept(source, values));
    }

    public boolean isInteractive() {
        return interactive.getAsBoolean();
    }

    public void setBaseUrl(String url) {
        this.url.set("manual", url);
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

    public ConfigValue getConfigFileName() {
        return config;
    }

    public ConfigValue getWorkingDirConfigValue() {
        return workingDir;
    }

    public ConfigValue getBaseUrlConfigValue() {
        return url;
    }

    public int waitTimeout() {
        return waitTimeout.getAsInt();
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

    public ConfigValue getRemoveWebtauFromUserAgent() {
        return removeWebtauFromUserAgent;
    }

    public Path getDocArtifactsPath() {
        return getWorkingDir().resolve(docPath.getAsPath());
    }

    public boolean isAnsiEnabled() {
        return !noColor.getAsBoolean();
    }

    public int getWindowWidth() {
        return windowWidth.getAsInt();
    }

    public int getWindowHeight() {
        return windowHeight.getAsInt();
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

    public boolean isHeadless() {
        return headless.getAsBoolean();
    }

    public Path getChromeBinPath() {
        return chromeBinPath.getAsPath();
    }

    public Path getChromeDriverPath() {
        return chromeDriverPath.getAsPath();
    }

    public List<String> getEnvPath() {
        return envPath.getAsList();
    }

    @Override
    public String toString() {
        return enumeratedCfgValues.values().stream().map(ConfigValue::toString).collect(Collectors.joining("\n"));
    }

    public void print() {
        int maxKeyLength = enumeratedCfgValues.values().stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getKey().length()).max(Integer::compareTo).orElse(0);

        int maxValueLength = enumeratedCfgValues.values().stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getAsString().length()).max(Integer::compareTo).orElse(0);

        enumeratedCfgValues.values().stream().filter(ConfigValue::nonDefault).forEach(v -> {
                    String valueAsText = v.getAsString();
                    int valuePadding = maxValueLength - valueAsText.length();

                    ConsoleOutputs.out(Color.BLUE, String.format("%" + maxKeyLength + "s", v.getKey()), ": ",
                            Color.YELLOW, valueAsText,
                            StringUtils.createIndentation(valuePadding),
                            FontStyle.NORMAL, " // from ", v.getSource());
                }
        );
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
                interactive,
                env,
                url,
                verbosityLevel,
                workingDir,
                waitTimeout,
                disableFollowingRedirects,
                maxRedirects,
                userAgent,
                removeWebtauFromUserAgent,
                docPath,
                reportPath,
                noColor,
                windowWidth,
                windowHeight,
                staleElementRetry,
                headless,
                chromeDriverPath,
                chromeBinPath,
                envPath);

        Stream<ConfigValue> additionalConfigValues = handlers.stream()
                .flatMap(WebTauConfigHandler::additionalConfigValues);

        return Stream.concat(standardConfigValues, additionalConfigValues)
                .collect(Collectors.toMap(ConfigValue::getKey, v -> v, (o, n) -> n, LinkedHashMap::new));
    }

    private static class CfgInstanceHolder {
        private static WebTauConfig INSTANCE = new WebTauConfig();
    }

    private static List<WebTauConfigHandler> discoverConfigHandlers() {
        return ServiceLoaderUtils.load(WebTauConfigHandler.class);
    }
}
