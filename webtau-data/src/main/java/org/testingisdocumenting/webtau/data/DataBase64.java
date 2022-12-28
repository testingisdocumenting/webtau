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

package org.testingisdocumenting.webtau.data;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DataBase64 {
    /**
     * Use <code>data.base64.encode</code> to encode a text value as base64
     * @return encoded value
     */
    public String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Use <code>data.base64.decode</code> to decode base64 back to text
     * @return decoded value
     */
    public String decode(String text) {
        return new String(Base64.getDecoder().decode(text));
    }
}
