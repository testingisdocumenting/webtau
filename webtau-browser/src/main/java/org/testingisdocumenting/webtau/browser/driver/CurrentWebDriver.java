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
import org.openqa.selenium.interactions.*;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.persona.Persona;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentWebDriver implements
        WebDriver,
        TakesScreenshot,
        JavascriptExecutor,
        WebStorage,
        Interactive,
        WebDriverCreatorListener {
    public static final CurrentWebDriver INSTANCE = new CurrentWebDriver();

    private final ThreadLocal<Map<String, WebDriver>> local;
    private final Map<String, WebDriver> global;

    private CurrentWebDriver() {
        local = ThreadLocal.withInitial(HashMap::new);
        global = new ConcurrentHashMap<>();

        WebDriverCreatorListeners.add(this);
    }

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
        WebDriverCreator.quit(getDriver());
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

    @Override
    public LocalStorage getLocalStorage() {
        return ((WebStorage)getDriver()).getLocalStorage();
    }

    @Override
    public SessionStorage getSessionStorage() {
        return ((WebStorage)getDriver()).getSessionStorage();
    }

    @Override
    public void beforeDriverCreation() {
    }

    @Override
    public void afterDriverCreation(WebDriver webDriver) {
    }

    @Override
    public void beforeDriverQuit(WebDriver webDriver) {
    }

    @Override
    public void afterDriverQuit(WebDriver webDriver) {
        Map<String, WebDriver> driverByPersona = local.get();
        driverByPersona.values().removeIf(driver -> driver == webDriver);
    }

    @Override
    public void perform(Collection<Sequence> actions) {
        ((Interactive)getDriver()).perform(actions);
    }

    @Override
    public void resetInputState() {
        ((Interactive)getDriver()).resetInputState();
    }

    public void setDriver(WebDriver driver) {
        Map<String, WebDriver> driverByPersonaId = local.get();

        Persona currentPersona = Persona.getCurrentPersona();
        driverByPersonaId.put(currentPersona.getId(), driver);
    }

    private WebDriver getDriver() {
        Map<String, WebDriver> driverByPersonaId = BrowserConfig.isSameDriverInThreads() ?
                global:
                local.get();

        Persona currentPersona = Persona.getCurrentPersona();
        WebDriver webDriver = driverByPersonaId.get(currentPersona.getId());

        if (webDriver != null) {
            return webDriver;
        }

        WebDriver newDriver = WebDriverCreator.create();
        driverByPersonaId.put(currentPersona.getId(), newDriver);

        return newDriver;
    }
}
