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

package org.testingisdocumenting.webtau.browser.documentation;

import org.testingisdocumenting.webtau.browser.page.PageElement;
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
