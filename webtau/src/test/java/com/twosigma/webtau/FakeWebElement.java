package com.twosigma.webtau;

import org.openqa.selenium.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FakeWebElement implements WebElement {
    private final String tagName;
    private final String text;
    private final Map<String, String> attrs;

    public FakeWebElement(String tagName, String text, Map<String, String> attrs) {
        this.tagName = tagName;
        this.text = text;
        this.attrs = attrs;
    }

    public static FakeWebElement tagAndText(String tagName, String text) {
        return new FakeWebElement(tagName, text, Collections.emptyMap());
    }

    @Override
    public void click() {
    }

    @Override
    public void submit() {
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
    }

    @Override
    public void clear() {
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public String getAttribute(String name) {
        return attrs.get(name);
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String s) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return null;
    }
}
