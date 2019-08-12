/*
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

package com.twosigma.webtau.browser;

import com.twosigma.webtau.browser.documentation.BrowserDocumentation;
import com.twosigma.webtau.browser.driver.CurrentWebDriver;
import com.twosigma.webtau.browser.navigation.BrowserPageNavigation;
import com.twosigma.webtau.browser.page.PageElement;
import com.twosigma.webtau.browser.page.PageUrl;
import com.twosigma.webtau.browser.page.path.ElementPath;
import com.twosigma.webtau.browser.page.path.GenericPageElement;
import com.twosigma.webtau.cache.Cache;
import com.twosigma.webtau.utils.UrlUtils;
import org.openqa.selenium.OutputType;

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static com.twosigma.webtau.reporter.TestStep.createAndExecuteStep;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Browser {
    private static final String DEFAULT_URL_CACHE_KEY = "current";

    private final InjectedJavaScript injectedJavaScript;

    public static final Browser browser = new Browser();
    public final CurrentWebDriver driver = CurrentWebDriver.INSTANCE;

    public final Cookies cookies = new Cookies(driver);
    public final LocalStorage localStorage = new LocalStorage(driver);
    public final BrowserDocumentation doc = new BrowserDocumentation(driver);

    public final PageUrl url = new PageUrl(driver::getCurrentUrl);

    private Browser() {
        injectedJavaScript = new InjectedJavaScript(driver);
    }

    public void open(String url) {
        String fullUrl = createFullUrl(url);

        String currentUrl = driver.getCurrentUrl();
        boolean sameUrl = fullUrl.equals(currentUrl);

        createAndExecuteStep(null, tokenizedMessage(action("opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action(sameUrl ? "staying at" : "opened"), urlValue(fullUrl)),
                () -> {
                    if (!sameUrl) {
                        BrowserPageNavigation.open(driver, url, fullUrl);
                    }
                });
    }

    public void reopen(String url) {
        String fullUrl = createFullUrl(url);

        createAndExecuteStep(null, tokenizedMessage(action("re-opening"), urlValue(fullUrl)),
                () -> tokenizedMessage(action("opened"), urlValue(fullUrl)),
                () -> BrowserPageNavigation.open(driver, url, fullUrl));
    }

    public void refresh() {
        createAndExecuteStep(null, tokenizedMessage(action("refreshing current page")),
                () -> tokenizedMessage(action("refreshed current page")),
                () -> BrowserPageNavigation.refresh(driver));
    }

    public void close() {
        createAndExecuteStep(null, tokenizedMessage(action("closing browser")),
                () -> tokenizedMessage(action("browser is closed")),
                driver::quit);
    }

    public void restart() {
        String currentUrl = driver.getCurrentUrl();

        createAndExecuteStep(null, tokenizedMessage(action("restarting browser")),
                () -> tokenizedMessage(action("browser is restarted")),
                () -> {
                    close();
                    browser.open(currentUrl);
                });
    }

    public void saveCurrentUrl() {
        saveCurrentUrl(DEFAULT_URL_CACHE_KEY);
    }

    public void saveCurrentUrl(String key) {
        createAndExecuteStep(null, tokenizedMessage(action("saving current url as"), stringValue(key)),
                () -> tokenizedMessage(action("saved current url as"), stringValue(key)),
                () -> {
                    Cache.cache.put(makeCacheKey(key), driver.getCurrentUrl());
                });
    }

    public void openSavedUrl() {
        openSavedUrl(DEFAULT_URL_CACHE_KEY);
    }

    public void openSavedUrl(String key) {
        createAndExecuteStep(null, tokenizedMessage(action("opening url saved as"), stringValue(key)),
                () -> tokenizedMessage(action("opened url saved as"), stringValue(key)),
                () -> {
                    Object url = Cache.cache.get(makeCacheKey(key));
                    if (url == null) {
                        throw new IllegalStateException("no previously saved url found");
                    }

                    reopen(url.toString());
                });
    }

    public PageElement $(String css) {
        return new GenericPageElement(driver, injectedJavaScript, ElementPath.css(css));
    }

    public boolean wasUsed() {
        return driver.wasUsed();
    }

    public String takeScreenshotAsBase64() {
        return driver.getScreenshotAs(OutputType.BASE64);
    }

    private String createFullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat(getCfg().getBaseUrl(), url);
    }

    private static String makeCacheKey(String givenKey) {
        return "url_" + givenKey;
    }
}
