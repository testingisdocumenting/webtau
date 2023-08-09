/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.table;

import org.jsoup.nodes.Element;

public class BrowserTableNode {
    private Element element;

    BrowserTableNode() {
    }

    void setElement(Element element) {
        this.element = element;
    }

    public String tagName() {
        return element.tagName();
    }

    public String attribute(String name) {
        return element.attributes().get(name);
    }

    public String html() {
        return element.html();
    }

    public String text() {
        return element.text();
    }
}
