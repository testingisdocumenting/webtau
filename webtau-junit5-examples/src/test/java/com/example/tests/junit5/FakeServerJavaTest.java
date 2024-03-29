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

import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;
import static org.testingisdocumenting.webtau.server.WebTauServerFacade.server;

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

        http.get(myServer.getBaseUrl() + "/bye/person?politeFactor=2", ((header, body) -> {
            body.get("message").should(equal("bye person"));
        }));
        // fake-response-check
    }

    @Test
    public void requestProperties() {
        WebTauRouter router = server.router()
                .get("/test/:name", (request) -> server.response(map(
                        "method", request.getMethod(),
                        "path", request.getPath(),
                        "query", request.getQuery(),
                        "fullUrl", request.getFullUrl(),
                        "queryParam1", request.queryParam("qp1"),
                        "queryParam2List", request.queryParamList("qp2"),
                        "pathWithQuery", request.getPathWithQuery())));

        WebTauServer fake = server.fake("local-fake", router);

        http.get(fake.getBaseUrl() + "/test/hello?qp1=one&qp2=two&qp2=two-two", ((header, body) -> {
            body.get("method").should(equal("GET"));
            body.get("path").should(equal("/test/hello"));
            body.get("query").should(equal("qp1=one&qp2=two&qp2=two-two"));
            body.get("queryParam1").should(equal("one"));
            body.get("queryParam2List").should(equal(list("two", "two-two")));
            body.get("fullUrl").should(equal(Pattern.compile("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+/test/hello\\?qp1=one&qp2=two")));
        }));

        fake.stop();
    }

    static WebTauRouter createRouter() {
        // router-example
        WebTauRouter router = server.router()
                .get("/hello/:name", (request) -> server.response(map("message", "hello " + request.param("name"))))
                .get("/bye/:name", (request) -> server.response(map("message", "bye " + request.param("name"))));
        // router-example

        return router;
    }
}
