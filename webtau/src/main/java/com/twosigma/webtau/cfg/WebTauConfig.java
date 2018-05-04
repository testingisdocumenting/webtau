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

package com.twosigma.webtau.cfg;

import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.console.ansi.FontStyle;
import com.twosigma.webtau.utils.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.webtau.cfg.ConfigValue.declare;

public class WebTauConfig {
    public static final WebTauConfig INSTANCE = new WebTauConfig();

    private final ConfigValue config = declare("config", "config path", () -> "test.cfg");
    private final ConfigValue env = declare("env", "environment id", () -> "local");
    private final ConfigValue url = declare("url", "base url for application under test", () -> null);
    private final ConfigValue waitTimeout = declare("waitTimeout", "wait timeout in milliseconds", () -> 5000);
    private final ConfigValue workingDir = declare("workingDir", "logical working dir", () -> Paths.get(""));
    private final ConfigValue docPath = declare("docPath", "path for screenshots and other generated " +
            "artifacts for documentation", workingDir::getAsPath);
    private final ConfigValue reportPath = declare("reportPath", "report file path", () -> getWorkingDir().resolve("webtau.report.html"));
    private final ConfigValue windowWidth = declare("windowWidth", "browser window width", () -> 1000);
    private final ConfigValue windowHeight = declare("windowHeight", "browser window height", () -> 800);
    private final ConfigValue headless = declare("headless", "run headless mode", () -> false);
    private final ConfigValue chromeDriverPath = declare("chromeDriverPath", "path to chrome driver binary", () -> null);
    private final ConfigValue chromeBinPath = declare("chromeBinPath", "path to chrome binary", () -> null);

    private final List<ConfigValue> enumeratedCfgValues = Arrays.asList(
            config,
            env,
            url,
            workingDir,
            waitTimeout,
            docPath,
            reportPath,
            windowWidth,
            windowHeight,
            headless,
            chromeDriverPath,
            chromeBinPath);

    private final List<ConfigValue> freeFormCfgValues = new ArrayList<>();

    protected WebTauConfig() {
        acceptConfigValues("environment variable", envVarsAsMap());
        acceptConfigValues("system property", systemPropsAsMap());
    }

    public Stream<ConfigValue> getCfgValuesStream() {
        return enumeratedCfgValues.stream();
    }

    public String get(String key) {
        Optional<ConfigValue> configValue = freeFormCfgValues.stream().filter(v -> v.match(key)).findFirst();
        return configValue.map(ConfigValue::getAsString).orElse("");
    }

    public void acceptConfigValues(String source, Map<String, ?> values) {
        enumeratedCfgValues.forEach(v -> v.accept(source, values));

        registerFreeFormCfgValues(values);
        freeFormCfgValues.forEach(v -> v.accept(source, values));
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

    public ConfigValue getWorkingDirConfigValue() {
        return workingDir;
    }

    public ConfigValue getBaseUrlConfigValue() {
        return url;
    }

    public int waitTimeout() {
        return waitTimeout.getAsInt();
    }

    public Path getDocArtifactsPath() {
        return docPath.getAsPath();
    }

    public int getWindowWidth() {
        return windowWidth.getAsInt();
    }

    public int getWindowHeight() {
        return windowHeight.getAsInt();
    }

    public Path getWorkingDir() {
        return workingDir.getAsPath();
    }

    public Path getReportPath() {
        return reportPath.getAsPath();
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

    @Override
    public String toString() {
        return enumeratedCfgValues.stream().map(ConfigValue::toString).collect(Collectors.joining("\n"));
    }

    public void print() {
        int maxKeyLength = enumeratedCfgValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getKey().length()).max(Integer::compareTo).orElse(0);

        int maxValueLength = enumeratedCfgValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getAsString().length()).max(Integer::compareTo).orElse(0);

        enumeratedCfgValues.stream().filter(ConfigValue::nonDefault).forEach(v -> {
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
                .filter(k -> noConfigValuePresent(enumeratedCfgValues, k));

        keys.filter(k -> noConfigValuePresent(freeFormCfgValues, k))
                .forEach(k -> {
                    ConfigValue configValue = declare(k, "free form cfg value", () -> null);
                    freeFormCfgValues.add(configValue);
                });
    }

    private boolean noConfigValuePresent(List<ConfigValue> configValues, String key) {
        return configValues.stream().noneMatch(cv -> cv.match(key));
    }

    private static Map<String, ?> systemPropsAsMap() {
        return System.getProperties().stringPropertyNames().stream()
                .collect(Collectors.toMap(n -> n, System::getProperty));
    }

    private static Map<String, ?> envVarsAsMap() {
        return System.getenv();
    }
}
