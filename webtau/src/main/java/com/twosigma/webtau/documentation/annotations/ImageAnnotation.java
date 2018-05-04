package com.twosigma.webtau.documentation.annotations;

import com.twosigma.webtau.page.PageElement;
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
    private String color = "red";
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
