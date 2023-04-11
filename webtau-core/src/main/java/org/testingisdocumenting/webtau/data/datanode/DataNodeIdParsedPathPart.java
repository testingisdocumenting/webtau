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

package org.testingisdocumenting.webtau.data.datanode;

public class DataNodeIdParsedPathPart {
    public enum PartType {
        PEER,
        CHILD
    }

    private final PartType type;
    private final Integer idx;
    private final String childName;

    public DataNodeIdParsedPathPart(PartType type, Integer idx, String childName) {
        this.type = type;
        this.idx = idx;
        this.childName = childName;
    }

    public PartType getType() {
        return type;
    }

    public Integer getIdx() {
        return idx;
    }

    public String getChildName() {
        return childName;
    }
}
