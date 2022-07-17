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
import org.testingisdocumenting.webtau.browser.page.PageElement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ImageAnnotation {
    public enum Placement {
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
    private final List<PageElement> pageElements;
    protected Placement placement;

    public ImageAnnotation(Stream<PageElement> pageElements, String type, String text) {
        this.id = type + idGen.incrementAndGet();
        this.pageElements = pageElements.collect(Collectors.toList());
        this.type = type;
        this.text = text;
        this.placement = Placement.Center;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<PageElement> getPageElements() {
        return Collections.unmodifiableList(pageElements);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageAnnotation above() {
        placement = Placement.Above;
        return this;
    }

    public ImageAnnotation below() {
        placement = Placement.Below;
        return this;
    }

    public ImageAnnotation toTheLeft() {
        placement = Placement.ToTheLeft;
        return this;
    }

    public ImageAnnotation toTheRight() {
        placement = Placement.ToTheRight;
        return this;
    }

    public abstract void addAnnotationData(Map<String, Object> data,
                                           List<WebElementLocationAndSizeProvider> locationAndSizeProviders);

    protected Point position(WebElementLocationAndSizeProvider locationAndSizeProvider) {
        switch (placement) {
            case Above:
                return above(locationAndSizeProvider.getLocation(), locationAndSizeProvider.getSize());
            case Below:
                return below(locationAndSizeProvider.getLocation(), locationAndSizeProvider.getSize());
            case ToTheLeft:
                return toTheLeft(locationAndSizeProvider.getLocation(), locationAndSizeProvider.getSize());
            case ToTheRight:
                return toTheRight(locationAndSizeProvider.getLocation(), locationAndSizeProvider.getSize());
            case Center:
            default:
                return center(locationAndSizeProvider.getLocation(), locationAndSizeProvider.getSize());
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
