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

package com.twosigma.webtau.http.json;

import com.twosigma.webtau.http.HttpRequestBody;
import com.twosigma.webtau.utils.JsonUtils;

import java.util.Map;

public class JsonRequestBody implements HttpRequestBody {
    private final String asString;

    public JsonRequestBody(Map<String, Object> data) {
        this.asString = JsonUtils.serialize(data);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String type() {
        return "application/json";
    }

    @Override
    public String asString() {
        return asString;
    }
}
