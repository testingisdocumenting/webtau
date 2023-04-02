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

package org.testingisdocumenting.webtau.websocket;

import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;

class WebSocketUtils {
    static Object convertForPrettyPrint(Object original) {
        if (!(original instanceof CharSequence)) {
            return original;
        }

        CharSequence text = (CharSequence) original;
        return convertToJsonIfPossible(text.toString());
    }

    static Object convertToJsonIfPossible(String message) {
        if (JsonUtils.looksLikeJson(message)) {
            try {
                return JsonUtils.deserialize(message);
            } catch (JsonParseException e) {
                return message;
            }
        }

        return message;
    }
}
