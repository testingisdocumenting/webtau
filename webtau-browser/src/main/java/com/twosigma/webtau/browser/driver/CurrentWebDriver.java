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

package com.twosigma.webtau.browser.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class CurrentWebDriver implements WebDriver, TakesScreenshot, JavascriptExecutor, WebStorage {
    private AtomicBoolean wasUsed = new AtomicBoolean(false);
    private ThreadLocal<WebDriver> local = ThreadLocal.withInitial(WebDriverCreator::create);

    @Override
    public void get(String url) {
        getDriver().get(url);
    }

    @Override
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return getDriver().getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getDriver().findElement(by);
    }

    @Override
    public String getPageSource() {
        return getDriver().getPageSource();
    }

    @Override
    public void close() {
        getDriver().close();
    }

    @Override
    public void quit() {
        WebDriverCreator.close(getDriver());
        local.set(null);
    }

    @Override
    public Set<String> getWindowHandles() {
        return getDriver().getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return getDriver().getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return getDriver().switchTo();
    }

    @Override
    public Navigation navigate() {
        return getDriver().navigate();
    }

    @Override
    public Options manage() {
        return getDriver().manage();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(outputType);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args);
    }

    public boolean wasUsed() {
        return wasUsed.get();
    }

    private WebDriver getDriver() {
        wasUsed.set(true);

        WebDriver webDriver = local.get();
        if (webDriver != null) {
            return webDriver;
        }

        WebDriver newDriver = WebDriverCreator.create();
        local.set(newDriver);

        return newDriver;
    }

    @Override
    public LocalStorage getLocalStorage() {
        return ((WebStorage)getDriver()).getLocalStorage();
    }

    @Override
    public SessionStorage getSessionStorage() {
        return ((WebStorage)getDriver()).getSessionStorage();
    }
}
