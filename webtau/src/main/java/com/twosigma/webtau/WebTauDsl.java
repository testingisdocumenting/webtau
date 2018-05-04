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

package com.twosigma.webtau;

import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.ranges.GreaterThanMatcher;
import com.twosigma.webtau.http.Http;
import com.twosigma.webtau.http.HttpUrl;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TestStep;
import com.twosigma.webtau.reporter.TokenizedMessage;
import com.twosigma.webtau.cfg.WebTauConfig;
import com.twosigma.webtau.documentation.DocumentationDsl;
import com.twosigma.webtau.driver.CurrentWebDriver;
import com.twosigma.webtau.expectation.VisibleValueMatcher;
import com.twosigma.webtau.page.Cookies;
import com.twosigma.webtau.page.PageElement;
import com.twosigma.webtau.page.path.ElementPath;
import com.twosigma.webtau.page.path.GenericPageElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.*;

public class WebTauDsl extends Ddjt {
    public static final WebTauConfig cfg = WebTauConfig.INSTANCE;

    public static final CurrentWebDriver driver = new CurrentWebDriver();
    public static final Http http = Http.http;
    public static final DocumentationDsl doc = new DocumentationDsl(driver);
    public static final Cookies cookies = new Cookies(driver);

    public static <E> void executeStep(E context,
                                       TokenizedMessage inProgressMessage,
                                       Supplier<TokenizedMessage> completionMessageSupplier,
                                       Runnable action) {
        TestStep<E> step = TestStep.create(context, inProgressMessage, completionMessageSupplier, action);
        step.execute(StepReportOptions.REPORT_ALL);
    }

    public static WebTauConfig getCfg() {
        return cfg;
    }

    public static void open(String url) {
        String fullUrl = createFullUrl(url);

        String currentUrl = driver.getCurrentUrl();
        boolean sameUrl = fullUrl.equals(currentUrl);

        executeStep(null, tokenizedMessage(action("opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action(sameUrl ? "staying at" : "opened"), urlValue(fullUrl)),
                () -> { if (! sameUrl) driver.get(fullUrl); });
    }

    public static void reopen(String url) {
        String fullUrl = createFullUrl(url);

        executeStep(null, tokenizedMessage(action("re-opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action("opened"), urlValue(fullUrl)),
                () -> driver.get(fullUrl));
    }

    public static boolean wasBrowserUsed() {
        return driver.wasUsed();
    }

    public static String takeScreenshotAsBase64() {
        return WebTauDsl.driver.getScreenshotAs(OutputType.BASE64);
    }

    public static PageElement $(String css) {
        return new GenericPageElement(driver, ElementPath.css(css));
    }

    public static ValueMatcher beVisible() {
        return new VisibleValueMatcher();
    }

    public static ValueMatcher getBeVisible() {
        return beVisible();
    }

    private static String createFullUrl(String url) {
        if (HttpUrl.isFull(url)) {
            return url;
        }

        return HttpUrl.concat(cfg.getBaseUrl(), url);
    }
}
