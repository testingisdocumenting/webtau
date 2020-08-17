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

package org.testingisdocumenting.webtau.browser.documentation;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.page.PageElement;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ImageAnnotation {
    public enum Position {
        Center,
        Above,
        Below,
        ToTheLeft,
        ToTheRight
    }

    private static final AtomicInteger idGen = new AtomicInteger();

    private final String id;
    private final String type;
    private String text;
    private String color = "a";
    private final PageElement pageElement;
    protected Position position;

    public ImageAnnotation(PageElement pageElement, String type, String text) {
        this.id = type + idGen.incrementAndGet();
        this.pageElement = pageElement;
        this.type = type;
        this.text = text;
        this.position = Position.Center;
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

    public ImageAnnotation above() {
        position = Position.Above;
        return this;
    }

    public ImageAnnotation below() {
        position = Position.Below;
        return this;
    }

    public ImageAnnotation toTheLeft() {
        position = Position.ToTheLeft;
        return this;
    }

    public ImageAnnotation toTheRight() {
        position = Position.ToTheRight;
        return this;
    }

    public ImageAnnotation withColor(String color) {
        this.color = color;
        return this;
    }

    public abstract void addAnnotationData(Map<String, Object> data, WebElement webElement);

    protected Point position(WebElement webElement) {
        switch (position) {
            case Above:
                return above(webElement.getLocation(), webElement.getSize());
            case Below:
                return below(webElement.getLocation(), webElement.getSize());
            case ToTheLeft:
                return toTheLeft(webElement.getLocation(), webElement.getSize());
            case ToTheRight:
                return toTheRight(webElement.getLocation(), webElement.getSize());
            case Center:
            default:
                return center(webElement.getLocation(), webElement.getSize());
        }
    }

    private Point center(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
    }

    private Point above(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2, location.getY());
    }

    private Point below(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight());
    }

    private Point toTheLeft(Point location, Dimension size) {
        return new Point(location.getX(), location.getY() + size.getHeight() / 2);
    }

    private Point toTheRight(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth(), location.getY() + size.getHeight() / 2);
    }
}
