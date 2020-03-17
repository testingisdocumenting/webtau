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

package com.twosigma.webtau.http;

import org.junit.Test;

import static com.twosigma.webtau.WebTauCore.*;
import static com.twosigma.webtau.http.Http.http;
import static com.twosigma.webtau.http.HttpOverloadsTestCommon.*;

public class HttpJavaOverloadsTest extends HttpTestBase {
    @Test
    public void getWithoutValidationSyntaxCheck() {
        http.get("/end-point");
        http.get("/end-point", http.query("queryParam1", "queryParamValue1"));
        http.get("/end-point", http.query("queryParam1", "queryParamValue1"), http.header("h1", "v1"));
        http.get("/end-point", http.header("h1", "v1"));
    }

    @Test
    public void putWithoutReturnOverloads() {
        http.put("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", query, requestBody, (header, body) -> {
            bodyValidation.accept(body);
            urlValidation.accept(body);
        });

        http.put("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyValidation.accept(body);
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
            bodyValidation.accept(body);
            pathValidation.accept(body);
        });

        http.put("/full-echo", (header, body) -> {
            pathValidation.accept(body);
        });
    }

    @Test
    public void putWithReturnOverloads() {
        Integer number;
        String text;

        number = http.put("/full-echo", query, requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_KEY);
        });
        actual(number).should(equal(BODY_EXPECTED_RETURN));

        number = http.put("/full-echo", query, requestBody, (header, body) -> {
            bodyValidation.accept(body);
            urlValidation.accept(body);

            return body.get(BODY_KEY);
        });
        actual(number).should(equal(BODY_EXPECTED_RETURN));

        number = http.put("/full-echo", requestHeader, requestBody, (header, body) -> {
            headerValidation.accept(body);
            bodyValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_KEY);
        });
        actual(number).should(equal(BODY_EXPECTED_RETURN));

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

        number = http.put("/full-echo", requestBody, (header, body) -> {
            bodyValidation.accept(body);
            pathValidation.accept(body);

            return body.get(BODY_KEY);
        });
        actual(number).should(equal(BODY_EXPECTED_RETURN));

        text = http.put("/full-echo", (header, body) -> {
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

            return header.statusCode();
        });
        actual(number).should(equal(expected));

        number = http.delete("/full-echo", requestHeader, (header, body) -> {
            headerValidation.accept(body);
            pathValidation.accept(body);

            return header.statusCode();
        });
        actual(number).should(equal(expected));

        number = http.delete("/full-echo", query, (header, body) -> {
            urlValidation.accept(body);

            return header.statusCode();
        });
        actual(number).should(equal(expected));

        number = http.delete("/full-echo", (header, body) -> {
            pathValidation.accept(body);

            return header.statusCode();
        });
        actual(number).should(equal(expected));
    }
}
