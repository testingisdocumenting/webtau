package com.twosigma.webtau;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class FakeWebDriver implements WebDriver {
    private Map<String, WebElement> fakesByCss = new HashMap<>();

    public void registerFakeElement(String css, WebElement webElement) {
        fakesByCss.put(css, webElement);
    }

    @Override
    public void get(String url) {
    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        if (by instanceof By.ByCssSelector) {
            String rendered = by.toString();
            int idxOfColon = rendered.indexOf(":");
            String css = rendered.substring(idxOfColon + 1).trim();
            return fakesByCss.containsKey(css) ? Collections.singletonList(fakesByCss.get(css)) : Collections.emptyList();
        }

        return Collections.emptyList();
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return null;
    }
}
