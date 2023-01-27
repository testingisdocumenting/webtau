/*
 * Copyright 2022 webtau maintainers
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

package com.example.tests.junit5;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.PageElementValue;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class BrowserTestContainerJavaTest {
    private final PageElement box = $("#search-box");
    private final PageElement results = $("#results .result");
    private final PageElementValue<Integer> numberOfResults = results.getCount();

    private static BrowserWebDriverContainer<?> seleniumContainer;

    @BeforeAll
    public static void setupDriverUsingTestContainer() {
        Testcontainers.exposeHostPorts(BrowserConfig.getBaseUrlPort());
        seleniumContainer = new BrowserWebDriverContainer<>()
                .withCapabilities(new FirefoxOptions());

        step("preparing selenium test container", () -> {
            seleniumContainer.start();
            browser.setDriver(new RemoteWebDriver(seleniumContainer.getSeleniumAddress(), new FirefoxOptions()));
        });
    }

    @AfterAll
    public static void shutdownContainer() {
        seleniumContainer.stop();
    }

    @Test
    public void search() {
        browser.open("/search");

        box.setValue("search this");
        box.sendKeys(browser.keys.enter);

        numberOfResults.waitToBe(greaterThan(1));
    }
}
