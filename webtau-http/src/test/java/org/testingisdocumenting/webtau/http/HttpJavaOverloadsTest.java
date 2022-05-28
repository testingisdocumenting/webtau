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

import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.http;
import static org.testingisdocumenting.webtau.http.HttpOverloadsTestCommon.*;

public class HttpJavaOverloadsTest extends HttpTestBase {
    @Test
    public void getWithoutValidationSyntaxCheck() {
        http.get("/end-point");
        http.get("/end-point", http.query("queryParam1", "queryParamValue1"));
        http.get("/end-point", http.query("queryParam1", "queryParamValue1"), http.header("h1", "v1"));
        http.get("/end-point", http.header("h1", "v1"));
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
}
