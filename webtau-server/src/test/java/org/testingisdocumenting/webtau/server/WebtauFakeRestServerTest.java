/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server;

import org.junit.Test;

import java.util.Collections;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;

public class WebtauFakeRestServerTest {
    @Test
    public void fixedUrlBasedResponse() {
        WebtauFakeRestServer restServer = new WebtauFakeRestServer("my-crud", 0);
        restServer.addOverride(new WebtauServerOverrideFake(
                "GET", "/customers", "application/json",
                "{\"customers\": []}"));

        restServer.start();

        try {
            http.get(restServer.getBaseUrl() + "/customers", (header, body) -> {
                body.get("customers").should(equal(Collections.emptyList()));
            });

            http.get(restServer.getBaseUrl() + "/abcd", (header, body) -> {
                header.statusCode().should(equal(404));
            });
        } finally {
            restServer.stop();
        }
    }

    @Test
    public void pathParamsBasedResponse() {
        WebtauFakeRestServer restServer = new WebtauFakeRestServer("my-crud", 0);
        restServer.getJson("/customer/{id}", (params) -> aMapOf("getId", params.get("id")));
        restServer.postJson("/customer/{id}", (params) -> aMapOf("postId", params.get("id")));
        restServer.putJson("/customer/{id}", (params) -> aMapOf("putId", params.get("id")));
        restServer.deleteJson("/customer/{id}", (params) -> aMapOf("deleteId", params.get("id")));
        restServer.patchJson("/customer/{id}", (params) -> aMapOf("patchId", params.get("id")));

        restServer.start();

        try {
            http.get(restServer.getBaseUrl() + "/customer/11", (header, body) -> {
                body.get("getId").should(equal("11"));
            });

            http.post(restServer.getBaseUrl() + "/customer/22", (header, body) -> {
                body.get("postId").should(equal("22"));
            });

            http.put(restServer.getBaseUrl() + "/customer/33", (header, body) -> {
                body.get("putId").should(equal("33"));
            });

            http.delete(restServer.getBaseUrl() + "/customer/44", (header, body) -> {
                body.get("deleteId").should(equal("44"));
            });

            http.patch(restServer.getBaseUrl() + "/customer/55", (header, body) -> {
                body.get("patchId").should(equal("55"));
            });
        } finally {
            restServer.stop();
        }
    }

    @Test
    public void pathParamsBasedResponseWithStatusCode() {
        WebtauFakeRestServer restServer = new WebtauFakeRestServer("my-crud", 0);
        restServer.getJson("/customer/{id}", (params) -> 203, (params) -> aMapOf("getId", params.get("id")));
        restServer.postJson("/customer/{id}", (params) -> 203, (params) -> aMapOf("postId", params.get("id")));
        restServer.putJson("/customer/{id}", (params) -> 203, (params) -> aMapOf("putId", params.get("id")));
        restServer.deleteJson("/customer/{id}", (params) -> 203, (params) -> aMapOf("deleteId", params.get("id")));
        restServer.patchJson("/customer/{id}", (params) -> 203, (params) -> aMapOf("patchId", params.get("id")));

        restServer.start();

        try {
            http.get(restServer.getBaseUrl() + "/customer/11", (header, body) -> {
                header.statusCode().should(equal(203));
                body.get("getId").should(equal("11"));
            });

            http.post(restServer.getBaseUrl() + "/customer/22", (header, body) -> {
                header.statusCode().should(equal(203));
                body.get("postId").should(equal("22"));
            });

            http.put(restServer.getBaseUrl() + "/customer/33", (header, body) -> {
                header.statusCode().should(equal(203));
                body.get("putId").should(equal("33"));
            });

            http.delete(restServer.getBaseUrl() + "/customer/44", (header, body) -> {
                header.statusCode().should(equal(203));
                body.get("deleteId").should(equal("44"));
            });

            http.patch(restServer.getBaseUrl() + "/customer/55", (header, body) -> {
                header.statusCode().should(equal(203));
                body.get("patchId").should(equal("55"));
            });
        } finally {
            restServer.stop();
        }
    }

    @Test
    public void shouldPreventFromRegisteringSamePath() {
        WebtauFakeRestServer restServer = new WebtauFakeRestServer("my-crud", 0);
        restServer.getJson("/customer/{id}", (params) -> aMapOf("id", params.get("id")));

        code(() ->
            restServer.getJson("/customer/{id}", (params) -> aMapOf("id", params.get("id")))
        ).should(throwException("already found an override for server: my-crud with override id: GET-/customer/{id}, " +
                "existing override: WebtauServerOverrideRouteFake{method='GET', route=/customer/{id}, " +
                "responseType='application/json'}"));
    }
}