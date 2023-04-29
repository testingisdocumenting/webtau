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

package org.testingisdocumenting.webtau.data.xml;

import java.util.List;

public class XmlNode {
    private final String tagName;
    private final List<XmlNode> children;
    private final List<XmlAttribute> attributes;
    private final String text;

    public XmlNode(String tagName, List<XmlNode> children, List<XmlAttribute> attributes, String text) {
        this.tagName = tagName;
        this.children = children;
        this.attributes = attributes;
        this.text = text;
    }

    public boolean isTextNode() {
        return tagName.equals("");
    }

    public String getTagName() {
        return tagName;
    }

    public List<XmlNode> getChildren() {
        return children;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }

    public String getText() {
        return text;
    }
}
