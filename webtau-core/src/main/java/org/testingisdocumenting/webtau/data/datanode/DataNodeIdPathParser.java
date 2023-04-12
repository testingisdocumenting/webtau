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

import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

class DataNodeIdPathParser {
    private DataNodeIdPathParser() {
    }

    public static List<DataNodeIdParsedPathPart> parse(String path) {
        List<DataNodeIdParsedPathPart> result = new ArrayList<>();
        String leftOver = path.trim();

        while (!leftOver.isEmpty()) {
            PartialParseResult partialParseResult = parsePartial(leftOver);
            result.add(partialParseResult.part);

            leftOver = partialParseResult.leftOver;
        }

        return result;
    }

    private static PartialParseResult parsePartial(String leftOver) {
        if (leftOver.startsWith("[")) {
            return extractIndex(leftOver);
        } else {
            return extractChild(removeLeadingDots(leftOver));
        }
    }

    private static PartialParseResult extractChild(String leftOver) {
        StringBuilder extractedName = new StringBuilder();
        int idx = 0;
        for (; idx < leftOver.length(); idx++) {
            char c = leftOver.charAt(idx);
            if (c == '.') {
                break;
            } else if (c == '[') {
                break;
            } else {
                extractedName.append(c);
            }
        }

        return new PartialParseResult(new DataNodeIdParsedPathPart(DataNodeIdParsedPathPart.PartType.CHILD, null, extractedName.toString()), leftOver.substring(idx));
    }

    private static PartialParseResult extractIndex(String leftOver) {
        StringBuilder extractedIdxStr = new StringBuilder();
        int idx = 1;
        for (; idx < leftOver.length(); idx++) {
            char c = leftOver.charAt(idx);
            if (c == ']') {
                break;
            } else if ((c >= '0' && c <= '9') || (c == '-' && idx == 1)) {
                extractedIdxStr.append(c);
            } else {
                throw new IllegalArgumentException("unexpected char, expected numbers 0-9 (including negative) or closing bracket ], but got: " + c);
            }
        }

        int extractedIdx = StringUtils.convertToNumber(extractedIdxStr.toString()).intValue();

        return new PartialParseResult(new DataNodeIdParsedPathPart(DataNodeIdParsedPathPart.PartType.PEER, extractedIdx, ""), leftOver.substring(idx + 1));
    }

    private static String removeLeadingDots(String leftOver) {
        if (!leftOver.startsWith(".")) {
            return leftOver;
        }

        int idx = 0;
        while (leftOver.charAt(idx) == '.') {
            idx++;
        }

        return leftOver.substring(idx);
    }

    private static class PartialParseResult {
        DataNodeIdParsedPathPart part;
        String leftOver;

        public PartialParseResult(DataNodeIdParsedPathPart part, String leftOver) {
            this.part = part;
            this.leftOver = leftOver;
        }
    }
}
