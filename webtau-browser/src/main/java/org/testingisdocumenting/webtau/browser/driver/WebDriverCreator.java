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

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.cleanup.DeferredCallsRegistration;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class WebDriverCreator {
    private static final WebDriverCreatorHandler defaultDriverCreatorHandler = new WebDriverDefaultCreatorHandler();
    private static final List<WebDriverCreatorHandler> driverCreatorHandlers = ServiceLoaderUtils.load(WebDriverCreatorHandler.class);

    private static final List<WebDriver> drivers = Collections.synchronizedList(new ArrayList<>());

    static {
        registerCleanup();
    }

    public static WebDriver create() {
        WebDriverCreatorListeners.beforeDriverCreation();

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("initializing").classifier("webdriver").forP().id(BrowserConfig.getBrowserId()),
                () -> tokenizedMessage().action("initialized").classifier("webdriver").forP().id(BrowserConfig.getBrowserId()),
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
        Optional<WebDriver> webDriver = driverCreatorHandlers.stream()
                .map(WebDriverCreatorHandler::createDriver)
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(defaultDriverCreatorHandler::createDriver);

        if (!webDriver.isPresent()) {
            throw new IllegalStateException("default driver creator must create a driver or throw an erro");
        }

        return webDriver.get();
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
        DeferredCallsRegistration.callAfterAllTests("closing", "closed", "browsers",
                () -> !drivers.isEmpty(),
                WebDriverCreator::quitAll);
    }
}
