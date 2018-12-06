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

package com.twosigma.webtau.browser.page;

import java.util.Map;

public class HtmlNode {
    public static final HtmlNode NULL = nullNode();

    private boolean present;
    private String tagName;
    private String value;
    private Map<String, String> attributes;
    private String innerHtml;

    private HtmlNode() {
    }

    @SuppressWarnings("unchecked")
    public HtmlNode(Map<String, ?> meta) {
        present = true;
        tagName = (String) meta.get("tagName");
        attributes = (Map<String, String>) meta.get("attributes");
        innerHtml = (String) meta.get("innerHtml");

        Object metaValue = meta.get("value");
        this.value = metaValue != null ? metaValue.toString() : null;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getInnerHtml() {
        return innerHtml;
    }

    public void setInnerHtml(String innerHtml) {
        this.innerHtml = innerHtml;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HtmlNode{" +
                "present=" + present +
                ", tagName='" + tagName + '\'' +
                ", value='" + value + '\'' +
                ", attributes=" + attributes +
                ", innerHtml='" + innerHtml + '\'' +
                '}';
    }

    private static HtmlNode nullNode() {
        HtmlNode htmlNode = new HtmlNode();
        htmlNode.present = false;
        return htmlNode;
    }
}
