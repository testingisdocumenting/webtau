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

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class WebDriverCreator {
    private static final String CHROME_DRIVER_PATH_KEY = "webdriver.chrome.driver";

    private static final List<WebDriver> drivers = Collections.synchronizedList(new ArrayList<>());

    static {
        registerCleanup();
    }

    public static WebDriver create() {
        WebDriverCreatorListeners.beforeDriverCreation();

        WebDriver driver = createChromeDriver();
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

        if (System.getProperty(CHROME_DRIVER_PATH_KEY) == null) {
            setupDriverManagerConfig();
            downloadDriverMessage("chrome");
            WebDriverManager.chromedriver().setup();
        }

        return new ChromeDriver(options);
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
        // setting size for headless chrome crashes chrome
        if (!BrowserConfig.isHeadless()) {
            driver.manage().window().setSize(new Dimension(
                    BrowserConfig.getWindowWidth(),
                    BrowserConfig.getWindowHeight()));
        }
    }

    private static void registerCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(WebDriverCreator::quitAll));
    }
}
