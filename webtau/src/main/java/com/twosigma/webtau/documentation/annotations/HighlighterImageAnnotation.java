/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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
