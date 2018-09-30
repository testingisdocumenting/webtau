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

package com.twosigma.webtau.browser;

import com.twosigma.webtau.browser.documentation.DocumentationDsl;
import com.twosigma.webtau.browser.driver.CurrentWebDriver;
import com.twosigma.webtau.browser.navigation.BrowserPageNavigation;
import com.twosigma.webtau.browser.page.PageElement;
import com.twosigma.webtau.browser.page.path.ElementPath;
import com.twosigma.webtau.browser.page.path.GenericPageElement;
import com.twosigma.webtau.utils.UrlUtils;
import org.openqa.selenium.OutputType;

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.urlValue;
import static com.twosigma.webtau.reporter.TestStep.createAndExecuteStep;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Browser {
    public static final Browser browser = new Browser();
    public final CurrentWebDriver driver = new CurrentWebDriver();
    public final Cookies cookies = new Cookies(driver);
    public final LocalStorage localStorage = new LocalStorage(driver);
    public final DocumentationDsl doc = new DocumentationDsl(driver);

    private final InjectedJavaScript injectedJavaScript = new InjectedJavaScript(driver);

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
                () -> driver.get(fullUrl));
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
}
