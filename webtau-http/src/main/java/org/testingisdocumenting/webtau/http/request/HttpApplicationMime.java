/*
 * Copyright 2022 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.http.request;

import org.testingisdocumenting.webtau.http.binary.BinaryRequestBody;
import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.text.TextRequestBody;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

public class HttpApplicationMime {
    public static final String JSON = "application/json";
    public static final String OCTET_STREAM = "application/octet-stream";
    public static final String PDF = "application/pdf";

    public HttpRequestBody json(String json) {
        return TextRequestBody.withType(JSON, json);
    }

    public HttpRequestBody json(String firstKey, Object firstValue, Object... rest) {
        return new JsonRequestBody(CollectionUtils.map(firstKey, firstValue, rest));
    }

    public HttpRequestBody octetStream(byte[] content) {
        return BinaryRequestBody.withType(OCTET_STREAM, content);
    }

    public HttpRequestBody pdf(byte[] content) {
        return BinaryRequestBody.withType(PDF, content);
    }
}
