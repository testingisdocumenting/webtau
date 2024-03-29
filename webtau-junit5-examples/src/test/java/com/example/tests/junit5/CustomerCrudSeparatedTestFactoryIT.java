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

package com.example.tests.junit5;

import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.junit5.WebTau;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@WebTau
public class CustomerCrudSeparatedTestFactoryIT {
    private static final HttpRequestBody customerPayload = http.json(
            "firstName", "FN",
            "lastName", "LN");

    private static final HttpRequestBody changedCustomerPayload = http.json(
            "firstName", "FN",
            "lastName", "NLN");

    @TestFactory
    public Stream<DynamicTest> crud() {
        AtomicInteger id = new AtomicInteger();

        return Stream.of(
                dynamicTest("create", () -> {
                    id.set(http.post("/customers", customerPayload, ((header, body) -> {
                        return body.get("id").get();
                    })));
                }),

                dynamicTest("read", () -> {
                    http.get("/customers/" + id, ((header, body) -> {
                        body.should(equal(customerPayload));
                    }));
                }),

                dynamicTest("update", () -> {
                    http.put("/customers/" + id, changedCustomerPayload, ((header, body) -> {
                        body.should(equal(changedCustomerPayload));
                    }));

                    http.get("/customers/" + id, ((header, body) -> {
                        body.should(equal(changedCustomerPayload));
                    }));
                }),

                dynamicTest("delete", () -> {
                    http.delete("/customers/" + id, ((header, body) -> {
                        header.statusCode.should(equal(204));
                    }));

                    http.get("/customers/" + id, ((header, body) -> {
                        header.statusCode.should(equal(404));
                    }));
                })
        );
    }
}
