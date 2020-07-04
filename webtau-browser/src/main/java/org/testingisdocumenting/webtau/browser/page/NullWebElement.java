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

package org.testingisdocumenting.webtau.browser.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NullWebElement implements WebElement {
    private static final String NULL_VALUE = "[null value] element is not present on the page";
    private final String id;

    public NullWebElement(String id) {
        this.id = id;
    }

    @Override
    public void click() {
        error("click");
    }

    @Override
    public void submit() {
        error("submit");
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        error("send " + Arrays.toString(charSequences) + " keys");
    }

    @Override
    public void clear() {
        error("clear");
    }

    @Override
    public String getTagName() {
        return NULL_VALUE;
    }

    @Override
    public String getAttribute(String s) {
        return NULL_VALUE;
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
        return NULL_VALUE;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return Collections.emptyList();
    }

    @Override
    public WebElement findElement(By by) {
        return new NullWebElement(by.toString());
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public Point getLocation() {
        return new Point(0, 0);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(0, 0);
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(0, 0, 0, 0);
    }

    @Override
    public String getCssValue(String s) {
        return NULL_VALUE;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        error("screenshotAs");
        return null;
    }

    public void error(String action) {
        throw new ElementNotFoundException("can't " + action + " as element is not found: " + id + ". Try to wait for it to appear first.");
    }
}
