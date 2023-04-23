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

package org.testingisdocumenting.webtau.browser;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.*;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*;
import static org.testingisdocumenting.webtau.utils.UrlUtils.*;

public class BrowserConfig implements WebTauConfigHandler {
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";

    private static final Supplier<Object> NULL_DEFAULT = () -> null;

    private static final ConfigValue browserUrl = declare("browserUrl", "browser base url for application under test. It is being used" +
            " instead of url when provided", () -> "");

    private static final ConfigValue browserWidth = declare("browserWidth", "browser window width", () -> 0);
    private static final ConfigValue browserHeight = declare("browserHeight", "browser window height", () -> 0);
    private static final ConfigValue browserHeadless = declareBoolean("browserHeadless", "run browser in headless mode", false);

    private static final ConfigValue browserId = declare("browserId", "browser to use: chrome, firefox", () -> CHROME);
    private static final ConfigValue browserVersion = declare("browserVersion", "browser version for automatic driver download", () -> "");
    private static final ConfigValue browserRemoteDriverUrl = declare("browserRemoteDriverUrl", "browser remote driver url", () -> "");

    private static final ConfigValue browserSameDriverInThreads = declare("browserSameDriverInThreads", "set to true if you want only a single browser across multiple threads. Use case is notebook like apps." +
                    "Personas will still have their own drivers", () -> false);

    private static final ConfigValue disableExtensions = declare("browserDisableExtensions", "run browser without extensions", () -> false);

    private static final ConfigValue staleElementRetry = declare("browserStaleElementRetry", "number of times to automatically retry for browser stale element actions", () -> 5);
    private static final ConfigValue staleElementRetryWait = declare("browserStaleElementRetryWait", "wait time in between browser stale element retries", () -> 100);

    private static final ConfigValue chromeBinPath = declare("chromeBinPath", "path to chrome binary", NULL_DEFAULT);
    private static final ConfigValue chromeDriverPath = declare("chromeDriverPath", "path to chrome driver binary", NULL_DEFAULT);

    private static final ConfigValue firefoxBinPath = declare("firefoxBinPath", "path to firefox binary", NULL_DEFAULT);
    private static final ConfigValue firefoxDriverPath = declare("firefoxDriverPath", "path to firefox driver binary", NULL_DEFAULT);

    public static String getBrowserId() {
        return browserId.getAsString();
    }

    public static String getBrowserVersion() {
        return browserVersion.getAsString();
    }

    public static boolean isChrome() {
        return CHROME.equals(browserId.getAsString());
    }

    public static boolean isFirefox() {
        return FIREFOX.equals(browserId.getAsString());
    }

    public static void setBrowserUrl(String url) {
        browserUrl.setAndReport("manual", url);
    }

    public static String getBaseUrl() {
        return !BrowserConfig.getBrowserUrl().isEmpty() ?
                BrowserConfig.getBrowserUrl():
                getCfg().getBaseUrl();
    }

    public static String createFullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat(getBaseUrl(), url);
    }

    public static int getBaseUrlPort() {
        if (!BrowserConfig.getBrowserUrl().isEmpty()) {
            return extractPort(BrowserConfig.getBrowserUrl());
        }

        return extractPort(getCfg().getBaseUrl());
    }

    public static String getBrowserUrl() {
        return browserUrl.getAsString();
    }

    public static int getBrowserWidth() {
        return browserWidth.getAsInt();
    }

    public static int getBrowserHeight() {
        return browserHeight.getAsInt();
    }

    public static int getStaleElementRetry() {
        return staleElementRetry.getAsInt();
    }

    public static int getStaleElementRetryWait() {
        return staleElementRetryWait.getAsInt();
    }

    public static boolean isHeadless() {
        return browserHeadless.getAsBoolean();
    }

    public static boolean isRemoteDriver() {
        return !getRemoteDriverUrl().isEmpty();
    }

    public static boolean isSameDriverInThreads() {
        return browserSameDriverInThreads.getAsBoolean();
    }

    public static void setBrowserSameDriverInThreads(boolean isSame) {
        browserSameDriverInThreads.set("manual", isSame);
    }

    public static String getRemoteDriverUrl() {
        return browserRemoteDriverUrl.getAsString();
    }

    public static boolean areExtensionsDisabled() {
        return disableExtensions.getAsBoolean();
    }

    public static void setHeadless(boolean isHeadless) {
        browserHeadless.set("manual", isHeadless);
    }

    public static Path getChromeBinPath() {
        return chromeBinPath.getAsPath();
    }

    public static Path getChromeDriverPath() {
        return chromeDriverPath.getAsPath();
    }

    public static Path getFirefoxBinPath() {
        return firefoxBinPath.getAsPath();
    }

    public static Path getFirefoxDriverPath() {
        return firefoxDriverPath.getAsPath();
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(
                browserId,
                browserVersion,
                browserRemoteDriverUrl,
                browserUrl,
                browserWidth,
                browserHeight,
                browserHeadless,
                browserSameDriverInThreads,
                staleElementRetry,
                staleElementRetryWait,
                disableExtensions,
                chromeDriverPath,
                chromeBinPath,
                firefoxDriverPath,
                firefoxBinPath);
    }
}
