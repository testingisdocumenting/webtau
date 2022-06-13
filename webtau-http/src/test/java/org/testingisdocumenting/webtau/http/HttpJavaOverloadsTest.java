/*
 * Copyright 2020 webtau maintainers
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

import org.junit.Test;
import org.testingisdocumenting.webtau.data.Data;

import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.data.Data.*;
import static org.testingisdocumenting.webtau.http.Http.http;
import static org.testingisdocumenting.webtau.http.HttpOverloadsTestCommon.*;

public class HttpJavaOverloadsTest extends HttpTestBase {
    @Test
    public void getWithoutReturnOverloads() {
        http.get("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);
        });

        http.get("/full-echo", queryAsMap, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);
        });

        http.get("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);
        });

        http.get("/full-echo", query, (header, body) -> {
            pathValidation.accept(body);
        });

        http.get("/full-echo", queryAsMap, (header, body) -> {
            pathValidation.accept(body);
        });

        http.get("/full-echo", (header, body) -> {
            pathValidation.accept(body);
        });
    }

    @Test
    public void getWithReturnOverloads() {
        String text;

        text = http.get("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);

            return body.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        text = http.get("/full-echo", queryAsMap, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);

            return body.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        text = http.get("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));

        text = http.get("/full-echo", query, (header, body) -> {
            pathValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));

        text = http.get("/full-echo", queryAsMap, (header, body) -> {
            pathValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));

        text = http.get("/full-echo", (header, body) -> {
            pathValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));
    }

    @Test
    public void postWithoutReturnOverloads() {
        http.post("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);
        });

        http.post("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);
        });

        http.post("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);
        });

        http.post("/full-echo", (header, body) -> {
            pathValidation.accept(body);
        });
    }

    @Test
    public void postWithReturnOverloads() {
        Map<String, Object> map;
        List<Object> list;
        String text;

        map = http.post("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.post("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.post("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        map = http.post("/full-echo", query, requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.post("/full-echo", query, requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.post("/full-echo", query, requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        map = http.post("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.post("/full-echo", requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.post("/full-echo", requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        text = http.post("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);

            return header.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        text = http.post("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));

        text = http.post("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);

            return header.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        map = http.post("/full-echo", requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.post("/full-echo", requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.post("/full-echo", requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        text = http.post("/full-echo", (header, body) -> {
            pathValidation.accept(body);
            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));
    }
    
    @Test
    public void putWithoutReturnOverloads() {
        http.put("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);
        });

        http.put("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", (header, body) -> {
            pathValidation.accept(body);
        });
    }

    @Test
    public void putWithReturnOverloads() {
        Map<String, Object> map;
        List<Object> list;
        String text;

        map = http.put("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.put("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.put("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        map = http.put("/full-echo", query, requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.put("/full-echo", query, requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.put("/full-echo", query, requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        map = http.put("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.put("/full-echo", requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.put("/full-echo", requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        text = http.put("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);

            return header.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        text = http.put("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));

        text = http.put("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);

            return header.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        map = http.put("/full-echo", requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.put("/full-echo", requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.put("/full-echo", requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        text = http.put("/full-echo", (header, body) -> {
            pathValidation.accept(body);
            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));
    }


    @Test
    public void patchWithoutReturnOverloads() {
        http.patch("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);
        });

        http.patch("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);
        });

        http.patch("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);
        });

        http.patch("/full-echo", (header, body) -> {
            pathValidation.accept(body);
        });
    }

    @Test
    public void patchWithReturnOverloads() {
        Map<String, Object> map;
        List<Object> list;
        String text;

        map = http.patch("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.patch("/full-echo", query, requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.patch("/full-echo", query, requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        map = http.patch("/full-echo", query, requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.patch("/full-echo", query, requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.patch("/full-echo", query, requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        map = http.patch("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.patch("/full-echo", requestHeader, requestBodyMap, (header, body) -> {
            headerValidation.accept(body);
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.patch("/full-echo", requestHeader, requestBodyList, (header, body) -> {
            headerValidation.accept(body);
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        text = http.patch("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);

            return header.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        text = http.patch("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);

            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));

        text = http.patch("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);

            return header.get(HEADER_KEY);
        });
        actual(text).should(equal(HEADER_EXPECTED_RETURN));

        map = http.patch("/full-echo", requestBody, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        map = http.patch("/full-echo", requestBodyMap, (header, body) -> {
            bodyAsMapValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(map).should(equal(requestBodyMap));

        list = http.patch("/full-echo", requestBodyList, (header, body) -> {
            bodyAsListValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_RESPONSE_KEY);
        });
        actual(list).should(equal(requestBodyList));

        text = http.patch("/full-echo", (header, body) -> {
            pathValidation.accept(body);
            return body.get(PATH_KEY);
        });
        actual(text).should(equal(PATH_EXPECTED_RETURN));
    }

    @Test
    public void deleteWithoutReturnOverloads() {
        http.delete("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);
        });

        http.delete("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);
        });

        http.delete("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);
        });

        http.delete("/full-echo", (header, body) -> {
            pathValidation.accept(body);
        });
    }

    @Test
    public void deleteWithReturnOverloads() {
        Integer number;
        Integer expected = 200;

        number = http.delete("/full-echo", query, requestHeader, (header, body) -> {
            headerValidation.accept(body);
            urlValidation.accept(body);

            return header.statusCode;
        });
        actual(number).should(equal(expected));

        number = http.delete("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);

            return header.statusCode;
        });
        actual(number).should(equal(expected));

        number = http.delete("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);

            return header.statusCode;
        });
        actual(number).should(equal(expected));

        number = http.delete("/full-echo", (header, body) -> {
            pathValidation.accept(body);

            return header.statusCode;
        });
        actual(number).should(equal(expected));
    }

    @Test
    public void getWithoutValidationSyntaxCheck() {
        http.get("/end-point");
        http.get("/end-point", queryAsMap);
        http.get("/end-point", query);
        http.get("/end-point", queryAsMap, http.header("h1", "v1"));
        http.get("/end-point", query, http.header("h1", "v1"));
        http.get("/end-point", http.header("h1", "v1"));
    }

    @Test
    public void postWithoutValidation() {
        http.post("/full-echo", query, requestHeader, requestBody);
        http.post("/full-echo", query, requestHeader);
        http.post("/full-echo", query, requestBody);
        http.post("/full-echo", query, requestHeader);
        http.post("/full-echo", requestHeader);
        http.post("/full-echo", query, requestBody);
        http.post("/full-echo", requestHeader, requestBody);
        http.post("/full-echo", query);
        http.post("/full-echo", requestBody);
        http.post("/full-echo");

        http.post("/full-echo", query, requestHeader, requestBodyMap);
        http.post("/full-echo", query, requestBodyMap);
        http.post("/full-echo", requestBodyMap);
        http.post("/full-echo", requestHeader, requestBodyMap);
    }

    @Test
    public void putWithoutValidation() {
        http.put("/full-echo", query, requestHeader, requestBody);
        http.put("/full-echo", query, requestHeader);
        http.put("/full-echo", query, requestBody);
        http.put("/full-echo", query, requestHeader);
        http.put("/full-echo", requestHeader);
        http.put("/full-echo", query, requestBody);
        http.put("/full-echo", requestHeader, requestBody);
        http.put("/full-echo", query);
        http.put("/full-echo", requestBody);
        http.put("/full-echo");

        http.put("/full-echo", query, requestHeader, requestBodyMap);
        http.put("/full-echo", query, requestBodyMap);
        http.put("/full-echo", requestBodyMap);
        http.put("/full-echo", requestHeader, requestBodyMap);
    }

    @Test
    public void getFullReturnSyntaxExample() {
        String id = http.get("/query", http.query("q1", "v1"), http.header("h1", "v1"), ((header, body) -> {
            body.get("price").should(equal(100)); // validation
            return body.get("id"); // optional return
        }));
    }

    @Test
    public void getNoHeaderSyntaxExample() {
        http.get("/query", http.query("q1", "v1"), ((header, body) -> {
            body.get("price").should(equal(100));
        }));
    }

    @Test
    public void getNoQuerySyntaxExample() {
        http.get("/query", http.header("h1", "v1"), ((header, body) -> {
            body.get("price").should(equal(100));
        }));
    }

    @Test
    public void getOnlyValidationSyntaxExample() {
        http.get("/query", ((header, body) -> {
            body.get("price").should(equal(100));
        }));
    }

    @Test
    public void getOnlySyntaxExample() {
        http.get("/query");
    }

    @Test
    public void postFullReturnSyntaxExample() {
        String id = http.post("/chat", http.query("q1", "v1"), http.header("h1", "v1"), http.json("message", "hello"),
                (header, body) -> {
            body.get("status").should(equal("SUCCESS")); // validation
            return body.get("id"); // optional return
        });
    }

    @Test
    public void postNoHeaderSyntaxExample() {
        http.post("/chat", http.query("q1", "v1"), http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void postNoQuerySyntaxExample() {
        http.post("/chat", http.header("h1", "v1"), http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void postBodyOnlySyntaxExample() {
        http.post("/chat", http.json("message", "hello", "priority", "high"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void postBodyFromFileSyntaxExample() {
        http.post("/chat", data.json.map("chat-message.json"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void postValidationOnlySyntaxExample() {
        http.post("/chat", (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void postStatusCodeSyntaxExample() {
        http.post("/resource", (header, body) -> {
            header.statusCode.should(equal(200)); // explicit check that override default 201 (for POST) implicit check
        });
    }

    @Test
    public void postNoValidationSyntaxExample() {
        http.post("/chat");
    }

    @Test
    public void putFullReturnSyntaxExample() {
        String id = http.put("/chat/id1", http.query("q1", "v1"), http.header("h1", "v1"), http.json("message", "hello"),
                (header, body) -> {
            body.get("status").should(equal("SUCCESS")); // validation
            return body.get("id"); // optional return
        });
    }

    @Test
    public void putNoHeaderSyntaxExample() {
        http.put("/chat/id1", http.query("q1", "v1"), http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void putNoQuerySyntaxExample() {
        http.put("/chat/id1", http.header("h1", "v1"), http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void putBodyOnlySyntaxExample() {
        http.put("/chat/id1", http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void putValidationOnlySyntaxExample() {
        http.put("/chat/id1", (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void putNoValidationSyntaxExample() {
        http.put("/chat/id1");
    }

    @Test
    public void deleteFullReturnSyntaxExample() {
        String id = http.delete("/chat/id1", http.query("q1", "v1"), http.header("h1", "v1"), ((header, body) -> {
            body.get("status").should(equal("SUCCESS")); // validation
            return body.get("id"); // optional return
        }));
    }

    @Test
    public void deleteNoHeaderSyntaxExample() {
        http.delete("/chat/id1", http.query("q1", "v1"), ((header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        }));
    }

    @Test
    public void deleteNoQuerySyntaxExample() {
        http.delete("/chat/id1", http.header("h1", "v1"), ((header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        }));
    }

    @Test
    public void deleteOnlyValidationSyntaxExample() {
        http.delete("/chat/id1", ((header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        }));
    }

    @Test
    public void deleteOnlySyntaxExample() {
        http.delete("/chat/id1");
    }

    @Test
    public void patchFullReturnSyntaxExample() {
        String id = http.patch("/chat/id1", http.query("q1", "v1"), http.header("h1", "v1"), http.json("message", "hello"),
                (header, body) -> {
            body.get("status").should(equal("SUCCESS")); // validation
            return body.get("id"); // optional return
        });
    }

    @Test
    public void patchNoHeaderSyntaxExample() {
        http.patch("/chat/id1", http.query("q1", "v1"), http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void patchNoQuerySyntaxExample() {
        http.patch("/chat/id1", http.header("h1", "v1"), http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void patchBodyOnlySyntaxExample() {
        http.patch("/chat/id1", http.json("message", "hello"), (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void patchValidationOnlySyntaxExample() {
        http.patch("/chat/id1", (header, body) -> {
            body.get("status").should(equal("SUCCESS"));
        });
    }

    @Test
    public void patchNoValidationSyntaxExample() {
        http.patch("/chat/id1");
    }
}
