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

import java.util.List;
import java.util.Map;

public class ValueExtractorByPath {
    private ValueExtractorByPath() {
    }

    @SuppressWarnings("unchecked")
    public static <R> R extractFromMapOrList(Object mapOrList, String path) {
        Object result = mapOrList;

        List<DataNodeIdParsedPathPart> parts = DataNodeIdPathParser.parse(path);
        for (DataNodeIdParsedPathPart part : parts) {
            switch (part.getType()) {
                case PEER:
                    if (!(result instanceof List)) {
                        return null;
                    }

                    List<?> list = (List<?>) result;

                    int idx = part.getIdx();
                    if (idx < 0) {
                        idx = list.size() + idx;
                    }

                    if (idx > list.size()) {
                        return null;
                    }

                    result = list.get(idx);
                    break;
                case CHILD:
                    if (!(result instanceof Map)) {
                        return null;
                    }

                    result = ((Map<String, ?>) result).get(part.getChildName());
                    break;
            }
        }

        return (R) result;
    }

    public static DataNode extractFromDataNode(DataNode dataNode, String path) {
        DataNode result = dataNode;

        List<DataNodeIdParsedPathPart> parts = DataNodeIdPathParser.parse(path);
        for (DataNodeIdParsedPathPart part : parts) {
            switch (part.getType()) {
                case PEER:
                    result = result.get(part.getIdx());
                    break;
                case CHILD:
                    result = result.get(part.getChildName());
                    break;
            }
        }

        return result;
    }
}
