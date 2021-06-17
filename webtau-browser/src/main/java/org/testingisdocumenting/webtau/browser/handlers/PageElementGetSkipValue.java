/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.handlers;

import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath;

/**
 * get value handler receives all the element that match the PageElement {@link PageElementPath}
 * usually we associate each element associated with a path and its values. e.g. `$("ul li")` could match N elements
 * and {@link PageElement#elementValues()} will return N values
 *
 * but there are elements like Radio Button, that has N elements to denote a single value, so when we call
 * {@link PageElement#elementValues()} on it we should only get one with the selected value.
 *
 * this is a marker class
 */
public class PageElementGetSkipValue {
    public static final PageElementGetSkipValue INSTANCE = new PageElementGetSkipValue();

    private PageElementGetSkipValue() {
    }
}
