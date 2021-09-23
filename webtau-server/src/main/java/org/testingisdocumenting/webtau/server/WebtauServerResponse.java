/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server;

import java.util.HashMap;
import java.util.Map;

public class WebtauServerResponse {
    private final int statusCode;
    private final String contentType;
    private final byte[] content;
    private final Map<String, CharSequence> header;

    public WebtauServerResponse(int statusCode,
                                String contentType, byte[] content,
                                Map<String, CharSequence> header) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.content = content;
        this.header = new HashMap<>(header);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public Map<String, CharSequence> getHeader() {
        return header;
    }

    public WebtauServerResponse header(Map<String, CharSequence> header) {
        this.header.putAll(header);
        return this;
    }

    public WebtauServerResponse newResponseWithUpdatedStatusCodeIfRequired(String method) {
        if (statusCode != 0) {
            return this;
        }

        return new WebtauServerResponse(calcStatusCode(method), contentType, content, header);
    }

    private int calcStatusCode(String method) {
        if (method.equals("POST")) {
            return 201;
        }

        if (method.equals("GET")) {
            return 200;
        }

        if (content == null || content.length == 0) {
            return 204;
        }

        return 200;
    }
}
