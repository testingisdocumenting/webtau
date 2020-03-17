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

package org.testingisdocumenting.webtau.browser;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public interface AdditionalBrowserInteractions {
    void flashWebElements(List<WebElement> webElements);

    List<Map<String, ?>> extractElementsMeta(List<WebElement> webElements);

    List<WebElement> filterByText(List<WebElement> webElements, String text);

    List<WebElement> filterByRegexp(List<WebElement> webElements, String regexp);
}
