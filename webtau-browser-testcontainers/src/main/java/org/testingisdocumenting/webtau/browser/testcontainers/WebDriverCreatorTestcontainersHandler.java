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

package org.testingisdocumenting.webtau.browser.testcontainers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.browser.driver.WebDriverCreatorHandler;
import org.testingisdocumenting.webtau.cleanup.DeferredCallsRegistration;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

public class WebDriverCreatorTestcontainersHandler implements WebDriverCreatorHandler {
    private static final List<BrowserWebDriverContainer<?>> createdContainers = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Optional<WebDriver> createDriver() {
        if (!BrowserTestContainersConfig.isEnabled()) {
            return Optional.empty();
        }

        Capabilities capabilities = createCapabilities();

        int port = BrowserConfig.getBaseUrlPort();

        TokenizedMessage testContainersId = tokenizedMessage().id("test containers");
        TokenizedMessage webDriverClassifier = tokenizedMessage().classifier("webdriver");

        WebTauStep step = createStep(tokenizedMessage().action("creating").add(testContainersId).add(webDriverClassifier),
                () -> tokenizedMessage().action("created").add(testContainersId).add(webDriverClassifier),
                () -> {
                    step("expose test containers port", map("port", port),
                            () -> Testcontainers.exposeHostPorts(port));

                    BrowserWebDriverContainer<?> seleniumContainer = step("start container", () -> {
                        BrowserWebDriverContainer<?> container = new BrowserWebDriverContainer<>()
                                .withCapabilities(capabilities);

                        createdContainers.add(container);
                        LazyCleanupRegistration.INSTANCE.noOp();

                        container.start();
                        return container;
                    });

                    WebDriver driver = step("create remote driver",
                            () -> new RemoteWebDriver(seleniumContainer.getSeleniumAddress(), capabilities));

                    return Optional.of(driver);
                });

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private Capabilities createCapabilities() {
        if (BrowserConfig.isFirefox()) {
            return new FirefoxOptions();
        }

        return new ChromeOptions();
    }

    private static class LazyCleanupRegistration {
        private static final LazyCleanupRegistration INSTANCE = new LazyCleanupRegistration();

        private LazyCleanupRegistration() {
            DeferredCallsRegistration.callAfterAllTests("stopping", "stopped", "selenium test containers",
                    () -> !createdContainers.isEmpty(),
                    () -> createdContainers.forEach(BrowserWebDriverContainer::stop));
        }

        private void noOp() {
        }
    }
}
