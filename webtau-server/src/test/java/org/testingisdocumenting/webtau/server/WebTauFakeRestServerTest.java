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
import org.testingisdocumenting.webtau.server.route.WebTauRouter;

import java.util.Collections;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;
import static org.testingisdocumenting.webtau.server.WebTauServerFacade.*;

public class WebTauFakeRestServerTest {
    @Test
    public void fixedUrlBasedResponse() {
        try (WebTauFakeRestServer restServer = new WebTauFakeRestServer("my-crud", 0)) {
            restServer.addOverride(new WebTauServerOverrideFake(
                    "GET", "/customers",
                    new WebTauServerResponse(200, "application/json", "{\"customers\": []}".getBytes(),
                            Collections.emptyMap())));

            restServer.start();

            http.get(restServer.getBaseUrl() + "/customers", (header, body) -> {
                body.get("customers").should(equal(Collections.emptyList()));
            });

            http.get(restServer.getBaseUrl() + "/abcd", (header, body) -> {
                header.statusCode.should(equal(404));
            });
        }
    }

    @Test
    public void pathParamsBasedResponse() {
        WebTauRouter router = server.router("customers")
                .get("/customer/{id}", (request) -> server.response(aMapOf("getId", request.param("id"))))
                .post("/customer/{id}", (request) -> server.response(aMapOf("postId", request.param(("id")))))
                .put("/customer/{id}", (request) -> server.response(aMapOf("putId", request.param(("id")))))
                .delete("/customer/{id}", (request) -> server.response(aMapOf("deleteId", request.param(("id")))))
                .patch("/customer/{id}", (request) -> server.response(aMapOf("patchId", request.param(("id")))));

        try (WebTauServer restServer = server.fake("route-crud", router)) {
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
        }
    }

    @Test
    public void pathParamsBasedResponseWithStatusCode() {
        WebTauRouter router = server.router("customers")
                .get("/customer/{id}", (request) -> server.response(203, aMapOf("getId", request.param("id"))))
                .post("/customer/{id}", (request) -> server.response(203, aMapOf("postId", request.param(("id")))))
                .put("/customer/{id}", (request) -> server.response(203, aMapOf("putId", request.param(("id")))))
                .delete("/customer/{id}", (request) -> server.response(203, aMapOf("deleteId", request.param(("id")))))
                .patch("/customer/{id}", (request) -> server.response(203, aMapOf("patchId", request.param(("id")))));

        try (WebTauServer restServer = server.fake("route-crud-status-code", router)) {
            http.get(restServer.getBaseUrl() + "/customer/11", (header, body) -> {
                header.statusCode.should(equal(203));
                body.get("getId").should(equal("11"));
            });

            http.post(restServer.getBaseUrl() + "/customer/22", (header, body) -> {
                header.statusCode.should(equal(203));
                body.get("postId").should(equal("22"));
            });

            http.put(restServer.getBaseUrl() + "/customer/33", (header, body) -> {
                header.statusCode.should(equal(203));
                body.get("putId").should(equal("33"));
            });

            http.delete(restServer.getBaseUrl() + "/customer/44", (header, body) -> {
                header.statusCode.should(equal(203));
                body.get("deleteId").should(equal("44"));
            });

            http.patch(restServer.getBaseUrl() + "/customer/55", (header, body) -> {
                header.statusCode.should(equal(203));
                body.get("patchId").should(equal("55"));
            });
        }
    }

    @Test
    public void shouldPreventFromRegisteringSamePath() {
        WebTauRouter router = server.router("customers");
        router.get("/customer/{id}", (request) -> server.response(aMapOf("id", request.param("id"))));

        code(() ->
                router.get("/customer/{id}", (request) -> server.response(aMapOf("id", request.param("id"))))
        ).should(throwException("already found an override for list id: customers, with override id: GET-/customer/{id}, " +
                "existing override: WebTauServerOverrideRouteFake{method='GET', route=/customer/{id}}"));
    }

    @Test
    public void shouldPreventFromRegisteringSameRouter() {
        WebTauFakeRestServer restServer = new WebTauFakeRestServer("route-crud-duplicate-router-check", 0);
        WebTauRouter router = new WebTauRouter("customers");
        router.post("/customer/{id}", (request) -> server.response(aMapOf("postId", request.param("id"))));
        router.put("/customer/{id}", (request) -> server.response(aMapOf("putId", request.param("id"))));

        restServer.addOverride(router);

        code(() ->
                restServer.addOverride(router)
        ).should(throwException("already found an override for server: route-crud-duplicate-router-check, " +
                "with override id: customers, existing override: id:customers; list:\n" +
                "POST-/customer/{id}\n" +
                "PUT-/customer/{id}"));
    }
}