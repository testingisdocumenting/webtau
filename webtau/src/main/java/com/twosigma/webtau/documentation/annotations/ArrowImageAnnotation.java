package com.twosigma.webtau.documentation.annotations;

import com.twosigma.webtau.page.PageElement;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.Map;

public class ArrowImageAnnotation extends ImageAnnotation {
    public ArrowImageAnnotation(PageElement pageElement, String text) {
        super(pageElement, "arrow", text);
    }

    @Override
    public void addAnnotationData(Map<String, Object> data, WebElement webElement) {
        Point location = center(webElement);

        data.put("beginX", location.getX() - 50);
        data.put("beginY", location.getY() + 90);
        data.put("endX", location.getX());
        data.put("endY", location.getY() + 8);
    }
}
