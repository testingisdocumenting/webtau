/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datanode

class GroovyBodyDataNode extends GroovyDataNode {
    private final String textContent
    private final byte[] binaryContent

    GroovyBodyDataNode(String textContent, byte[] binaryContent, DataNode node) {
        super(node)
        this.textContent = textContent
        this.binaryContent = binaryContent
    }

    /**
     * Access to the raw textual content. Do not use it for business logic validation.
     * @return raw text content
     */
    String getTextContent() {
        return textContent
    }

    /**
     * Access to the raw binary content. Do not use it for business logic validation.
     * @return raw binary content, null if response is not binary
     */
    byte[] getBinaryContent() {
        return binaryContent
    }
}
