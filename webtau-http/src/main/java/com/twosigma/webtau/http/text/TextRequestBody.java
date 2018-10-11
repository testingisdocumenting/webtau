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

package com.twosigma.webtau.http.text;

import com.twosigma.webtau.http.request.HttpRequestBody;

public class TextRequestBody implements HttpRequestBody {
    private final String type;
    private final String content;

    public static TextRequestBody withType(String type, String content) {
        return new TextRequestBody(type, content);
    }

    private TextRequestBody(String type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String asString() {
        return content;
    }
}
