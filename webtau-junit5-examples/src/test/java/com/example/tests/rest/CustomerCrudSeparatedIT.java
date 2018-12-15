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

package com.example.tests.rest;

import com.twosigma.webtau.junit5.WebTau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.webtau.WebTauDsl.equal;
import static com.twosigma.webtau.WebTauDsl.http;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@WebTau
@DisplayName("customer")
public class CustomerCrudSeparatedIT {
    private static final Map<String, Object> customerPayload = createCustomerPayload(); // creating payload as a map
    private static final Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();

    private int id;

    @BeforeEach // junit5 doesn't have yet order of tests feature, so we create a customer for every test
    void resourcePreparation() {
        id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id").getTraceableValue();
        }));
    }

    @Test
    void create() {
        assertNotEquals(0, id);
    }

    @Test
    void read() {
        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload));
        }));
    }

    @Test
    void update() {
        http.put("/customers/" + id, changedCustomerPayload, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));
    }

    @Test
    void delete() {
        http.delete("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(404));
        }));
    }

    private static Map<String, Object> createCustomerPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("firstName", "FN");
        payload.put("lastName", "LN");

        return payload;
    }

    private static Map<String, Object> createChangedCustomerPayload() {
        Map<String, Object> payload = createCustomerPayload();
        payload.put("lastName", "NLN");

        return payload;
    }
}
