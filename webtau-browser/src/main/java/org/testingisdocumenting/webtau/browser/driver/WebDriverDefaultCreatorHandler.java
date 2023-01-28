/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testingisdocumenting.webtau.browser.BrowserConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class WebDriverDefaultCreatorHandler implements WebDriverCreatorHandler {
    private static final String CHROME_DRIVER_PATH_KEY = "webdriver.chrome.driver";
    private static final String FIREFOX_DRIVER_PATH_KEY = "webdriver.gecko.driver";

    @Override
    public Optional<WebDriver> createDriver() {
        return Optional.of(BrowserConfig.isRemoteDriver() ?
                createRemoteDriver() :
                createLocalDriver());
    }

    private static WebDriver createRemoteDriver() {
        if (BrowserConfig.isChrome()) {
            return createRemoteChromeDriver();
        }

        if (BrowserConfig.isFirefox()) {
            return createRemoteFirefoxDriver();
        }

        return throwUnsupportedBrowser();
    }

    private static WebDriver createLocalDriver() {
        if (BrowserConfig.isChrome()) {
            return createLocalChromeDriver();
        }

        if (BrowserConfig.isFirefox()) {
            return createLocalFirefoxDriver();
        }

        return throwUnsupportedBrowser();
    }

    private static WebDriver throwUnsupportedBrowser() {
        throw new IllegalArgumentException("unsupported browser: " + BrowserConfig.getBrowserId());
    }

    private static ChromeDriver createLocalChromeDriver() {
        ChromeOptions options = createChromeOptions();

        if (BrowserConfig.getChromeBinPath() != null) {
            options.setBinary(BrowserConfig.getChromeBinPath().toFile());
        }

        if (BrowserConfig.getChromeDriverPath() != null) {
            System.setProperty(CHROME_DRIVER_PATH_KEY, BrowserConfig.getChromeDriverPath().toString());
        }

        if (System.getProperty(CHROME_DRIVER_PATH_KEY) == null) {
            setupDriverManagerConfig();

            WebDriverManager driverManager = WebDriverManager.chromedriver();
            if (!BrowserConfig.getBrowserVersion().isEmpty()) {
                driverManager.browserVersion(BrowserConfig.getBrowserVersion());
            }

            driverManager.setup();
        }

        return new ChromeDriver(options);
    }

    private static RemoteWebDriver createRemoteChromeDriver() {
        ChromeOptions options = createChromeOptions();
        return createRemoteDriver(options);
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        if (BrowserConfig.isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
        }

        if (BrowserConfig.areExtensionsDisabled()) {
            options.addArguments("--disable-extensions");
            options.setExperimentalOption("useAutomationExtension", false);
        }

        return options;
    }

    private static FirefoxDriver createLocalFirefoxDriver() {
        FirefoxOptions options = createFirefoxOptions();

        if (BrowserConfig.getFirefoxBinPath() != null) {
            options.setBinary(BrowserConfig.getFirefoxBinPath());
        }

        if (BrowserConfig.getFirefoxDriverPath() != null) {
            System.setProperty(FIREFOX_DRIVER_PATH_KEY, BrowserConfig.getFirefoxDriverPath().toString());
        }

        if (System.getProperty(FIREFOX_DRIVER_PATH_KEY) == null) {
            setupDriverManagerConfig();
            WebDriverManager.firefoxdriver().setup();
        }

        return new FirefoxDriver(options);
    }

    private static FirefoxOptions createFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        if (BrowserConfig.isHeadless()) {
            options.setHeadless(true);
        }

        return options;
    }

    private static RemoteWebDriver createRemoteFirefoxDriver() {
        FirefoxOptions options = createFirefoxOptions();
        return createRemoteDriver(options);
    }

    private static RemoteWebDriver createRemoteDriver(Capabilities options) {
        try {
            return new RemoteWebDriver(new URL(BrowserConfig.getRemoteDriverUrl()), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setupDriverManagerConfig() {
        System.setProperty("wdm.forceCache", "true");
    }
}
