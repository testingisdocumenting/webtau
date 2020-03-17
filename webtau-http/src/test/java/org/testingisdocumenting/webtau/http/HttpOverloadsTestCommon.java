/*
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

package org.testingisdocumenting.webtau.http;

import org.testingisdocumenting.webtau.WebTauCore;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpQueryParams;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static org.testingisdocumenting.webtau.WebTauCore.equal;
import static org.testingisdocumenting.webtau.http.Http.http;

public class HttpOverloadsTestCommon {
    public static final String BODY_KEY = "b";
    public static final Integer BODY_EXPECTED_RETURN = 2;

    public static final String HEADER_KEY = "v";
    public static final String HEADER_EXPECTED_RETURN = "42";

    public static final String PATH_KEY = "urlPath";
    public static final String PATH_EXPECTED_RETURN = "/full-echo";

    public static final HttpQueryParams query = http.query("a", "1", "b", "text");
    public static final Map<String, ?> queryAsMap = WebTauCore.aMapOf("a", "1", "b", "text");
    public static final HttpHeader requestHeader = http.header(HEADER_KEY, HEADER_EXPECTED_RETURN);

    public static final Map<String, Object> requestBodyMap = Collections.singletonMap(BODY_KEY, BODY_EXPECTED_RETURN);
    public static final HttpRequestBody requestBody = new JsonRequestBody(requestBodyMap);

    public static final Consumer<DataNode> headerValidation = (body) -> body.get(HEADER_KEY).should(equal(HEADER_EXPECTED_RETURN));
    public static final Consumer<DataNode> pathValidation = (body) -> body.get(PATH_KEY).should(equal(PATH_EXPECTED_RETURN));
    public static final Consumer<DataNode> queryValidation = (body) -> body.get("urlQuery").should(equal("a=1&b=text"));
    public static final Consumer<DataNode> bodyValidation = (body) -> body.get("b").should(equal(2));

    public static final Consumer<DataNode> urlValidation = (body) -> {
        pathValidation.accept(body);
        queryValidation.accept(body);
    };
}
