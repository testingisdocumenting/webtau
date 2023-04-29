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

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.StringUtils;
import org.testingisdocumenting.webtau.version.WebTauVersion;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.*;

public class WebTauConfig implements PrettyPrintable {
    private static final String SOURCE_MANUAL = "manual";

    public static final String CONFIG_FILE_DEPRECATED_DEFAULT = "webtau.cfg";
    public static final String CONFIG_FILE_NAME_DEFAULT = "webtau.cfg.groovy";

    private static final List<WebTauConfigHandler> handlers = discoverConfigHandlers();

    private static final Supplier<Object> NULL_DEFAULT = () -> null;

    private final ConfigValue config = declare("config", "config file path", () -> CONFIG_FILE_NAME_DEFAULT);
    private final ConfigValue env = declare("env", "environment id", () -> "local");
    private final ConfigValue url = declare("url", "base url for application under test", NULL_DEFAULT);

    private final ConfigValue httpProxy = declare("httpProxy", "http proxy host:port", NULL_DEFAULT);

    private final ConfigValue verbosityLevel = declare("verbosityLevel", "output verbosity level. " +
            "0 - no output; 1 - test names; 2 - first level steps; etc", () -> Integer.MAX_VALUE);
    private final ConfigValue fullStackTrace = declare("fullStackTrace", "print full stack trace to console",
            () -> false);
    private final ConfigValue disableConsoleOverallReport = declare("disableConsoleOverallReport", "do not print failed tests, overall summary and path to the generated report at the end", () -> false);

    private final ConfigValue tableVerticalSeparator = declare("tableVerticalSeparator", "string to use as a vertical separator when print TableData", () -> " \u2502 ");

    private final ConfigValue consolePayloadOutputLimit = declare("consolePayloadOutputLimit",
            "max number of lines to display in console for outputs (e.g. http response)", () -> 500);

    private final ConfigValue waitTick = declare("waitTick", "wait tick in milliseconds", () -> 100L);
    private final ConfigValue waitTimeout = declare("waitTimeout", "wait timeout in milliseconds", () -> 5000L);

    private final ConfigValue httpTimeout = declare("httpTimeout", "http connect and read timeout in milliseconds", () -> 30000);

    private final ConfigValue disableFollowingRedirects = declareBoolean("disableRedirects", "disable following of redirects from HTTP calls", false);
    private final ConfigValue maxRedirects = declare("maxRedirects", "Maximum number of redirects to follow for an HTTP call", () -> 20);
    private final ConfigValue userAgent = declare("userAgent", "User agent to send on HTTP requests",
            () -> "webtau/" + WebTauVersion.getVersion());
    private final ConfigValue removeWebTauFromUserAgent = declare("removeWebTauFromUserAgent",
            "By default webtau appends webtau and its version to the user-agent, this disables that part",
            () -> false);
    private final ConfigValue workingDir = declare("workingDir", "logical working dir", () -> Paths.get(""));
    private final ConfigValue cachePath = declare("cachePath", "user driven cache base dir",
            () -> workingDir.getAsPath().resolve(".webtau-cache"));

    private final ConfigValue docPath = declare("docPath", "path for captured request/responses, screenshots and other generated " +
            "artifacts for documentation", () -> workingDir.getAsPath().resolve("doc-artifacts"));
    private final ConfigValue noColor = declareBoolean("noColor", "disable ANSI colors", false);
    private final ConfigValue reportPath = declare("reportPath", "report file path", () -> getWorkingDir().resolve("webtau.report.html"));
    private final ConfigValue failedReportPath = declare("failedReportPath", "failed report file path", () -> null);
    private final ConfigValue reportName = declare("reportName", "report name to show", () -> "WebTau report");
    private final ConfigValue reportNameUrl = declare("reportNameUrl", "report name url to navigate to when clicked", () -> "");

    private final Map<String, ConfigValue> enumeratedCfgValues = enumerateRegisteredConfigValues();

    private final List<ConfigValue> freeFormCfgValues = new ArrayList<>();

    public static WebTauConfig getCfg() {
        return CfgInstanceHolder.INSTANCE;
    }

    /**
     * Handlers are automatically discovered using service loader.
     * Use this method to manually register additional config handler in front of the queue.
     *
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
        registeredHandlers().forEach(h -> h.onBeforeCreate(this));

        Map<String, ?> envVarValues = envVarsAsMap();
        acceptConfigValues("environment variable", envVarValues);
        acceptConfigValues("environment variable", convertWebTauEnvVarsToPropNames(envVarValues));

        acceptConfigValues("system property", systemPropsAsMap());

        registeredHandlers().forEach(h -> h.onAfterCreate(this));
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

    public void setVerbosityLevel(int level) {
        verbosityLevel.setAndReport("manual", level);
    }

    public boolean getFullStackTrace() {
        return fullStackTrace.getAsBoolean();
    }

    public int getConsolePayloadOutputLimit() {
        return consolePayloadOutputLimit.getAsInt();
    }

    public boolean isConsoleOverallReportDisabled() {
        return disableConsoleOverallReport.getAsBoolean();
    }

    public String getTableVerticalSeparator() {
        return tableVerticalSeparator.getAsString();
    }

    public void acceptConfigValues(String source, Map<String, ?> values) {
        acceptConfigValues(source, Persona.DEFAULT_PERSONA_ID, values);
    }

    public void acceptConfigValues(String source, String personaId, Map<String, ?> values) {
        enumeratedCfgValues.values().forEach(v -> v.accept(source, personaId, values));

        registerFreeFormCfgValues(values);
        freeFormCfgValues.forEach(v -> v.accept(source, personaId, values));
    }

    /**
     * set base url
     * @param url new url
     */
    public void setUrl(String url) {
        setUrl(SOURCE_MANUAL, url);
    }

    /**
     * set base url with specified source
     * @param source source for logging
     * @param url new url
     */
    public void setUrl(String source, String url) {
        this.url.setAndReport(source, url);
    }

    /**
     * base url
     * @return base url
     */
    public String getUrl() {
        return url.getAsString();
    }

    /**
     * use {@link #getUrl}
     * @deprecated
     * @return base url
     */
    @Deprecated
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

    public ConfigValue getHttpProxyConfigValue() {
        return httpProxy;
    }

    public boolean isHttpProxySet() {
        return !httpProxy.isDefault();
    }

    public long getWaitTimeout() {
        return waitTimeout.getAsLong();
    }

    public void setWaitTimout(long waitTimoutMs) {
        waitTimeout.setAndReport("manual", waitTimoutMs);
    }

    public long getWaitTick() {
        return waitTick.getAsLong();
    }

    public void setWaitTick(long waitTickMs) {
        waitTick.setAndReport("manual", waitTickMs);
    }

    public int getHttpTimeout() {
        return httpTimeout.getAsInt();
    }

    public ConfigValue getHttpTimeoutValue() {
        return httpTimeout;
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
        if (!removeWebTauFromUserAgent.getAsBoolean()) {
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

    public void setRemoveWebTauFromUserAgent(boolean remove) {
        this.removeWebTauFromUserAgent.set(SOURCE_MANUAL, remove);
    }

    public ConfigValue getRemoveWebtauFromUserAgentConfigValue() {
        return removeWebTauFromUserAgent;
    }

    public ConfigValue getDocArtifactsPathConfigValue() {
        return docPath;
    }

    public Path getDocArtifactsPath() {
        return getWorkingDir().resolve(docPath.getAsPath());
    }

    public void setDocArtifactsPath(Path path) {
       docPath.setAndReport("manual", path);
    }

    public boolean isAnsiEnabled() {
        return !noColor.getAsBoolean();
    }

    public Path getWorkingDir() {
        return workingDir.getAsPath().toAbsolutePath();
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

    public ConfigValue getCachePathValue() {
        return cachePath;
    }

    public Path getReportPath() {
        return fullPath(reportPath.getAsPath());
    }

    public void setReportPath(Path path) {
        reportPath.set("manual", path);
    }

    public Path getFailedReportPath() {
        if (failedReportPath.isDefault()) {
            return null;
        }

        return fullPath(failedReportPath.getAsPath());
    }

    public ConfigValue getReportPathConfigValue() {
        return reportPath;
    }

    public String getReportName() {
        return reportName.getAsString();
    }

    public String getReportNameUrl() {
        return reportNameUrl.getAsString();
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
        printConfig(new PrettyPrinter(0), enumeratedCfgValues.values());
    }

    private void printConfig(PrettyPrinter printer, Collection<ConfigValue> configValues) {
        int maxKeyLength = configValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getKey().length()).max(Integer::compareTo).orElse(0);

        int maxValueLength = configValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getAsString().length()).max(Integer::compareTo).orElse(0);

        configValues.stream().filter(ConfigValue::nonDefault).forEach(v -> {
                    String valueAsText = v.getAsString();
                    int valuePadding = maxValueLength - valueAsText.length();

                    printer.printLine(Color.BLUE, String.format("%" + maxKeyLength + "s", v.getKey()), ": ",
                            Color.YELLOW, valueAsText,
                            StringUtils.createIndentation(valuePadding),
                            FontStyle.RESET, " // from ", v.getSource());
                }
        );
    }

    private Stream<WebTauConfigHandler> registeredHandlers() {
        return handlers.stream();
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
                httpProxy,
                verbosityLevel,
                fullStackTrace,
                disableConsoleOverallReport,
                tableVerticalSeparator,
                workingDir,
                waitTick,
                waitTimeout,
                httpTimeout,
                disableFollowingRedirects,
                maxRedirects,
                userAgent,
                removeWebTauFromUserAgent,
                docPath,
                reportPath,
                reportName,
                reportNameUrl,
                failedReportPath,
                noColor,
                consolePayloadOutputLimit,
                cachePath);

        Stream<ConfigValue> additionalConfigValues = handlers.stream()
                .flatMap(WebTauConfigHandler::additionalConfigValues);

        return Stream.concat(standardConfigValues, additionalConfigValues)
                .collect(Collectors.toMap(ConfigValue::getKey, v -> v, (o, n) -> n, LinkedHashMap::new));
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printConfig(printer, freeFormCfgValues);
        printConfig(printer, enumeratedCfgValues.values());
    }

    private static class CfgInstanceHolder {
        private static final WebTauConfig INSTANCE = new WebTauConfig();
    }

    private static List<WebTauConfigHandler> discoverConfigHandlers() {
        return ServiceLoaderUtils.load(WebTauConfigHandler.class);
    }
}
