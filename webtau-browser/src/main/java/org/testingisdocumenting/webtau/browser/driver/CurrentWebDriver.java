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

import org.openqa.selenium.*;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.*;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.persona.Persona;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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
        withDriverHandlingRecreation(driver -> {
            driver.get(url);
            return null;
        });
    }

    @Override
    public String getCurrentUrl() {
        return withDriverHandlingRecreation(WebDriver::getCurrentUrl);
    }

    @Override
    public String getTitle() {
        return withDriverHandlingRecreation(WebDriver::getTitle);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return withDriverHandlingRecreation(driver -> driver.findElements(by));
    }

    @Override
    public WebElement findElement(By by) {
        return withDriverHandlingRecreation(driver -> driver.findElement(by));
    }

    @Override
    public String getPageSource() {
        return withDriverHandlingRecreation(WebDriver::getPageSource);
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
        return withDriverHandlingRecreation(WebDriver::getWindowHandles);
    }

    @Override
    public String getWindowHandle() {
        return withDriverHandlingRecreation(WebDriver::getWindowHandle);
    }

    @Override
    public TargetLocator switchTo() {
        return withDriverHandlingRecreation(WebDriver::switchTo);
    }

    @Override
    public Navigation navigate() {
        return withDriverHandlingRecreation(WebDriver::navigate);
    }

    @Override
    public Options manage() {
        return withDriverHandlingRecreation(WebDriver::manage);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return withDriverHandlingRecreation(driver -> ((TakesScreenshot) driver).getScreenshotAs(outputType));
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return withDriverHandlingRecreation(driver -> ((JavascriptExecutor) driver).executeScript(script, args));
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return withDriverHandlingRecreation(driver -> ((JavascriptExecutor) driver).executeAsyncScript(script, args));
    }

    @Override
    public LocalStorage getLocalStorage() {
        return withDriverHandlingRecreation(driver -> ((WebStorage) driver).getLocalStorage());
    }

    @Override
    public SessionStorage getSessionStorage() {
        return withDriverHandlingRecreation(driver -> ((WebStorage) driver).getSessionStorage());
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
        Map<String, WebDriver> driverByPersona = getPersonaDrivers();
        driverByPersona.values().removeIf(driver -> driver == webDriver);
    }

    @Override
    public void perform(Collection<Sequence> actions) {
        withDriverHandlingRecreation(driver -> {
            ((Interactive) driver).perform(actions);
            return null;
        });
    }

    @Override
    public void resetInputState() {
        withDriverHandlingRecreation(driver -> {
            ((Interactive) driver).resetInputState();
            return null;
        });
    }

    public void setDriver(WebDriver driver) {
        Map<String, WebDriver> driverByPersonaId = getPersonaDrivers();

        Persona currentPersona = Persona.getCurrentPersona();
        driverByPersonaId.put(currentPersona.getId(), driver);
    }

    private WebDriver getDriver() {
        Map<String, WebDriver> driverByPersonaId = getPersonaDrivers();

        Persona currentPersona = Persona.getCurrentPersona();
        WebDriver webDriver = driverByPersonaId.get(currentPersona.getId());

        if (webDriver != null) {
            return webDriver;
        }

        WebDriver newDriver = WebDriverCreator.create();
        driverByPersonaId.put(currentPersona.getId(), newDriver);

        return newDriver;
    }

    private <R> R withDriverHandlingRecreation(Function<WebDriver, R> func) {
        WebDriver driver = getDriver();
        try {
            return func.apply(driver);
        } catch (NoSuchWindowException e) {
            afterDriverQuit(driver);
            return func.apply(getDriver());
        }
    }

    private Map<String, WebDriver> getPersonaDrivers() {
        return BrowserConfig.isSameDriverInThreads() ?
                global:
                local.get();
    }
}
