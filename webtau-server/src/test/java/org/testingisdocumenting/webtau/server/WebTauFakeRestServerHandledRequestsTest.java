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
import org.testingisdocumenting.webtau.server.registry.WebTauServerHandledRequest;
import org.testingisdocumenting.webtau.server.route.WebTauRouter;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;
import static org.testingisdocumenting.webtau.server.WebTauServerFacade.*;

public class WebTauFakeRestServerHandledRequestsTest {
    @Test
    public void shouldWaitOnACall() throws InterruptedException {
        WebTauRouter router = new WebTauRouter("customers");
        router.get("/customer/{id}", (request) -> server.response(map("getId", request.param("id"))))
                .post("/customer/{id}", (request) -> server.response(map("postId", request.param("id"))))
                .put("/customer/{id}", (request) -> server.response(map("putId", request.param("id"))));

        try (WebTauServer restServer = server.fake("router-crud-journal", router)) {
            Thread thread = new Thread(() -> {
                http.get(restServer.getBaseUrl() + "/customer/id3", (header, body) -> {
                    body.get("getId").should(equal("id3"));
                });
            });

            thread.start();

            restServer.getJournal().GET.waitTo(contain("/customer/id3"));

            thread.join();

            restServer.getJournal().GET.should(contain("/customer/id3"));
        }
    }

    @Test
    public void shouldCaptureRequest() {
        WebTauRouter router = new WebTauRouter("customers");
        router.post("/customer", (request) -> server.response(null));

        try (WebTauServer restServer = server.fake("router-crud-journal-request", router)) {
            http.post(restServer.getBaseUrl() + "/customer", map("name", "new name"));

            actual(restServer.getJournal().getLastHandledRequest()
                    .getCapturedRequest()).should(equal("{\"name\":\"new name\"}"));
        }
    }

    @Test
    public void shouldCaptureResponse() {
        WebTauRouter router = new WebTauRouter("customers");
        router.get("/customer/{id}", (request) -> server.response(map("getId", request.param("id"))));
        try (WebTauServer restServer = server.fake("router-crud-journal-response", router)) {
            http.get(restServer.getBaseUrl() + "/customer/id3");

            WebTauServerHandledRequest handledRequest = restServer.getJournal().getLastHandledRequest();
            actual(handledRequest.getStatusCode()).should(equal(200));
            actual(handledRequest.getCapturedResponse()).should(equal("{\"getId\":\"id3\"}"));
        }
    }
}
