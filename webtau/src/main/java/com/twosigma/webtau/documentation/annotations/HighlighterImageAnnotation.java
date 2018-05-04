package com.twosigma.webtau.documentation.annotations;

import com.twosigma.webtau.page.PageElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.Map;

public class HighlighterImageAnnotation extends ImageAnnotation {
    public HighlighterImageAnnotation(PageElement pageElement) {
        super(pageElement, "rectangle", "");
    }

    @Override
    public void addAnnotationData(Map<String, Object> data, WebElement webElement) {
        Point location = webElement.getLocation();
        Dimension size = webElement.getSize();

        data.put("x", location.getX());
        data.put("y", location.getY());
        data.put("width", size.getWidth());
        data.put("height", size.getHeight());
    }
}
