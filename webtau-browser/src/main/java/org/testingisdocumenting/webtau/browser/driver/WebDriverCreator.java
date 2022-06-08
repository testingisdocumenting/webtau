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

package org.testingisdocumenting.webtau.browser.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.cleanup.CleanupRegistration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class WebDriverCreator {
    private static final String CHROME_DRIVER_PATH_KEY = "webdriver.chrome.driver";
    private static final String FIREFOX_DRIVER_PATH_KEY = "webdriver.gecko.driver";

    private static final List<WebDriver> drivers = Collections.synchronizedList(new ArrayList<>());

    static {
        registerCleanup();
    }

    public static WebDriver create() {
        WebDriverCreatorListeners.beforeDriverCreation();

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("initializing"), classifier("webdriver"), FOR, id(BrowserConfig.getBrowserId())),
                () -> tokenizedMessage(action("initialized"), classifier("webdriver"), FOR, id(BrowserConfig.getBrowserId())),
                () -> {
                    WebDriver driver = createDriverWithAutoRetry();
                    initState(driver);
                    register(driver);

                    return driver;
                });

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    public static void quitAll() {
        drivers.forEach(WebDriverCreator::quitWithoutRemove);
        drivers.clear();
    }

    public static boolean hasActiveBrowsers() {
        return !drivers.isEmpty();
    }

    static void quit(WebDriver driver) {
        quitWithoutRemove(driver);
        drivers.remove(driver);
    }

    private static void quitWithoutRemove(WebDriver driver) {
        WebDriverCreatorListeners.beforeDriverQuit(driver);

        try {
            driver.quit();
        } catch (Throwable ignore) {
        }

        WebDriverCreatorListeners.afterDriverQuit(driver);
    }

    // after selenium 4 upgrade driver is not being created 100% of the time in GitHub CI
    // maybe there is a hidden race condition in webtau feature tests
    // for now we add auto retry
    private static WebDriver createDriverWithAutoRetry() {
        int numberOfAttempts = 5;
        int attemptNumber = 1;
        while (attemptNumber < numberOfAttempts) {
            try {
                return createDriver();
            } catch (RuntimeException e) {
                // ignore
            }
            attemptNumber++;
        }

        return createDriver();
    }

    private static WebDriver createDriver() {
        return BrowserConfig.isRemoteDriver() ?
                createRemoteDriver() :
                createLocalDriver();
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

    private static void register(WebDriver driver) {
        drivers.add(driver);
        WebDriverCreatorListeners.afterDriverCreation(driver);
    }

    private static void initState(WebDriver driver) {
        if (!BrowserConfig.isHeadless() &&
                BrowserConfig.getBrowserWidth() > 0 &&
                BrowserConfig.getBrowserHeight() > 0) {
            driver.manage().window().setSize(new Dimension(
                    BrowserConfig.getBrowserWidth(),
                    BrowserConfig.getBrowserHeight()));
        }
    }

    private static void registerCleanup() {
        CleanupRegistration.registerForCleanup("closing", "closed", "browsers",
                () -> !drivers.isEmpty(),
                WebDriverCreator::quitAll);
    }
}
