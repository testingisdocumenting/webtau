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

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebDriverCreator {
    private static final String CHROME_DRIVER_PATH_KEY = "webdriver.chrome.driver";
    private static final String FIREFOX_DRIVER_PATH_KEY = "webdriver.gecko.driver";

    private static final List<WebDriver> drivers = Collections.synchronizedList(new ArrayList<>());

    static {
        registerCleanup();
    }

    public static WebDriver create() {
        WebDriverCreatorListeners.beforeDriverCreation();

        WebDriver driver = createDriver();
        initState(driver);

        return register(driver);
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

    private static WebDriver createDriver() {
        if (BrowserConfig.isChrome()) {
            return createChromeDriver();
        }

        if (BrowserConfig.isFirefox()) {
            return createFirefoxDriver();
        }

        throw new IllegalArgumentException("unsupported browser: " + BrowserConfig.getBrowserId());
    }

    private static ChromeDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        if (BrowserConfig.getChromeBinPath() != null) {
            options.setBinary(BrowserConfig.getChromeBinPath().toFile());
        }

        if (BrowserConfig.getChromeDriverPath() != null) {
            System.setProperty(CHROME_DRIVER_PATH_KEY, BrowserConfig.getChromeDriverPath().toString());
        }

        if (BrowserConfig.isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
        }

        if (BrowserConfig.areExtensionsDisabled()) {
            options.addArguments("--disable-extensions");
            options.setExperimentalOption("useAutomationExtension", false);
        }

        if (System.getProperty(CHROME_DRIVER_PATH_KEY) == null) {
            setupDriverManagerConfig();
            downloadDriverMessage("chrome");

            WebDriverManager driverManager = WebDriverManager.chromedriver();
            if (!BrowserConfig.getBrowserVersion().isEmpty()) {
                driverManager.browserVersion(BrowserConfig.getBrowserVersion());
            }

            driverManager.setup();
        }

        return new ChromeDriver(options);
    }

    private static FirefoxDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();

        if (BrowserConfig.getFirefoxBinPath() != null) {
            options.setBinary(BrowserConfig.getFirefoxBinPath());
        }

        if (BrowserConfig.getFirefoxDriverPath() != null) {
            System.setProperty(FIREFOX_DRIVER_PATH_KEY, BrowserConfig.getChromeDriverPath().toString());
        }

        if (BrowserConfig.isHeadless()) {
            options.setHeadless(true);
        }

        if (System.getProperty(FIREFOX_DRIVER_PATH_KEY) == null) {
            setupDriverManagerConfig();
            downloadDriverMessage("firefox");
            WebDriverManager.firefoxdriver().setup();
        }

        return new FirefoxDriver(options);
    }

    private static void downloadDriverMessage(String browser) {
        ConsoleOutputs.out(Color.BLUE, "preparing ", Color.YELLOW, browser, Color.BLUE, " WebDriver");
    }

    private static void setupDriverManagerConfig() {
        System.setProperty("wdm.forceCache", "true");
    }

    public static void quitAll() {
        drivers.forEach(WebDriverCreator::quitWithoutRemove);
        drivers.clear();
    }

    private static WebDriver register(WebDriver driver) {
        drivers.add(driver);
        WebDriverCreatorListeners.afterDriverCreation(driver);

        return driver;
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
        Runtime.getRuntime().addShutdownHook(new Thread(WebDriverCreator::quitAll));
    }
}
