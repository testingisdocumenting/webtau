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

package com.twosigma.webtau.browser.documentation;

import com.twosigma.webtau.browser.page.PageElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ImageAnnotation {
    private static AtomicInteger idGen = new AtomicInteger();

    private String id;
    private String type;
    private String text;
    private String color = "a";
    private PageElement pageElement;

    public ImageAnnotation(PageElement pageElement, String type, String text) {
        this.id = type + idGen.incrementAndGet();
        this.pageElement = pageElement;
        this.type = type;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public PageElement getPageElement() {
        return pageElement;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public ImageAnnotation withColor(String color) {
        this.color = color;
        return this;
    }

    public abstract void addAnnotationData(Map<String, Object> data, WebElement webElement);

    protected Point center(WebElement webElement) {
        Point location = webElement.getLocation();
        Dimension size = webElement.getSize();

        return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
    }
}
