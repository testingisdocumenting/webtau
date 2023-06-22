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

public class RouterPropertiesJavaTest {
    private static WebTauServer myServer;

    private static final WebTauRouter router = createRouter();

    @BeforeAll
    static void createFakeServer() {
        myServer = server.fake("my-rest-server", router);
        myServer.setAsBaseUrl();
    }

    @AfterAll
    static void stopFakeServer() {
        myServer.stop();
    }

    @Test
    public void validateProperties() {
        http.get(myServer.getBaseUrl() + "/hello/person", ((header, body) -> {
            body.get("message").should(equal("hello person"));
        }));

        http.get(myServer.getBaseUrl() + "/bye/person?title=X", ((header, body) -> {
            body.get("message").should(equal("bye X person"));
        }));
    }


    static WebTauRouter createRouter() {
        WebTauRouter router = server.router()
        // path-param-example
                .get("/hello/:name", (request) ->
                        server.response(map("message", "hello " +
                                request.param("name"))))
        // path-param-example
        // query-param-example
                .get("/bye/:name", (request) ->
                        server.response(map("message", "bye " +
                                request.queryParam("title") +
                                " " + request.param("name"))));
        // query-param-example

        return router;
    }
}
