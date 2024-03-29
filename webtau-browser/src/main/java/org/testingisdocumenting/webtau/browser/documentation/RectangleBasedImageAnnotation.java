/*
 * Copyright 2022 webtau maintainers
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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RectangleBasedImageAnnotation extends ImageAnnotation {
    public RectangleBasedImageAnnotation(PageElement pageElement, String type, String text) {
        super(Stream.of(pageElement), type, text);
    }

    @Override
    public void addAnnotationData(Map<String, Object> data, List<WebElementLocationAndSizeProvider> locationAndSizeProviders) {
        Point location = locationAndSizeProviders.get(0).getLocation();
        Dimension size = locationAndSizeProviders.get(0).getSize();

        data.put("beginX", location.getX());
        data.put("beginY", location.getY());
        data.put("endX", location.getX() + size.getWidth());
        data.put("endY", location.getY() + size.getHeight());
    }
}
