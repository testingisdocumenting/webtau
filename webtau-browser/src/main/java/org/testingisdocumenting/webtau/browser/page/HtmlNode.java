/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page;

import java.util.Map;

public class HtmlNode {
    private final String tagName;
    private final String innerHtml;
    private final String outerHtml;
    private final String innerText;
    private final String value;
    private final Map<String, String> attributes;

    public HtmlNode(String tagName, String innerHtml, String outerHtml, String innerText, String value, Map<String, String> attributes) {
        this.tagName = tagName;
        this.innerHtml = innerHtml;
        this.outerHtml = outerHtml;
        this.innerText = innerText;
        this.value = value;

        this.attributes = attributes;
    }

    public String getTagName() {
        return tagName;
    }

    public String getInnerHtml() {
        return innerHtml;
    }

    public String getOuterHtml() {
        return outerHtml;
    }

    public String getInnerText() {
        return innerText;
    }

    public String getValue() {
        return value;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getTypeAttribute() {
        return attributes.getOrDefault("type", "");
    }

    @Override
    public String toString() {
        return "HtmlNode{" +
                "tagName='" + tagName + '\'' +
                ", innerHtml='" + innerHtml + '\'' +
                ", outerHtml='" + outerHtml + '\'' +
                ", innerText='" + innerText + '\'' +
                ", value='" + value + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
