/*
 * Copyright 2022 webtau maintainers
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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.server.WebTauServer;
import org.testingisdocumenting.webtau.server.route.WebTauRouter;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

public class FakeServerJavaTest {
    private static WebTauServer myServer;

    private static final WebTauRouter router = createRouter();

    @BeforeAll
    static void createFakeServer() {
        // server-create-example
        myServer = server.fake("my-rest-server", router);
        // server-create-example

        myServer.setAsBaseUrl();
    }

    @AfterAll
    static void stopFakeServer() {
        myServer.stop();
    }

    @Test
    public void validateFakeResponses() {
        // fake-response-check
        http.get(myServer.getBaseUrl() + "/hello/person", ((header, body) -> {
            body.get("message").should(equal("hello person"));
        }));

        http.get(myServer.getBaseUrl() + "/bye/person", ((header, body) -> {
            body.get("message").should(equal("bye person"));
        }));
        // fake-response-check
    }

    static WebTauRouter createRouter() {
        // router-example
        WebTauRouter router = server.router()
                .get("/hello/:name", (request) -> server.response(aMapOf("message", "hello " + request.param("name"))))
                .get("/bye/:name", (request) -> server.response(aMapOf("message", "bye " + request.param("name"))));
        // router-example

        return router;
    }
}
