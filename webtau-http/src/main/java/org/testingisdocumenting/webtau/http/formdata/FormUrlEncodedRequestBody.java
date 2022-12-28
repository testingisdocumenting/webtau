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

package org.testingisdocumenting.webtau.http.formdata;

import org.testingisdocumenting.webtau.http.request.HttpQueryParams;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;

import java.util.Map;

public class FormUrlEncodedRequestBody implements HttpRequestBody {
    private final String encoded;

    public FormUrlEncodedRequestBody(Map<?, ?> values) {
        encoded = new HttpQueryParams(values).toString();
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String type() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    public boolean isEmpty() {
        return encoded.isEmpty();
    }

    @Override
    public String asString() {
        return encoded;
    }
}
