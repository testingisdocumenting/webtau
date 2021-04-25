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
    private final String value;
    private final String type;
    private final Map<String, String> attributes;
    private final String innerHtml;

    @SuppressWarnings("unchecked")
    public HtmlNode(Map<String, ?> meta) {
        tagName = (String) meta.get("tagName");
        attributes = (Map<String, String>) meta.get("attributes");
        innerHtml = (String) meta.get("innerHtml");

        value = extractValue(meta);
        type = attributes.getOrDefault("type", "");
    }

    public String getTagName() {
        return tagName;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getInnerHtml() {
        return innerHtml;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HtmlNode{" +
                "tagName='" + tagName + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                ", innerHtml='" + innerHtml + '\'' +
                '}';
    }

    private String extractValue(Map<String, ?> meta) {
        Object metaValue = meta.getOrDefault("value", null);
        return metaValue != null ? metaValue.toString() : null;
    }
}
