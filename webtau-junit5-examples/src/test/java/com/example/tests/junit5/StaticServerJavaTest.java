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

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauDsl.*;

public class StaticServerJavaTest {
    private static WebTauServer myServer;

    @BeforeAll
    static void createStaticServer() {
        myServer = createServer();
        // set-base-url-example
        myServer.setAsBaseUrl();
        // set-base-url-example
    }

    @AfterAll
    static void stopStaticServer() {
        myServer.stop();
    }

    @Test
    public void staticContentCheck() {
        http.get("/hello.html", (header, body) -> {
            body.should(equal("<body>\n" +
                    "<p>hello</p>\n" +
                    "</body>"));
        });

        // static-server-json
        http.get(myServer.getBaseUrl() + "/data.json", ((header, body) -> {
            body.get("type").should(equal("person"));
        }));
        // static-server-json
    }

    @Test
    public void browserStaticContent() {
        // browser-example
        browser.open("/hello.html");
        $("p").should(equal("hello"));
        // browser-example
    }

    @Test
    public void contentOverride() {
        // override-example
        WebTauRouter router = server.router()
                .get("/hello/:name", (request) -> server.response(map("message", "hello " + request.param("name"))));
        myServer.addOverride(router);
        // override-example

        http.get("/hello/world", (header, body) -> {
            body.get("message").should(equal("hello world"));
        });

        myServer.removeOverride(router);
    }

    @Test
    public void slowDown() {
        getCfg().getHttpTimeoutValue().set("test", 500);
        // mark-unresponsive
        myServer.markUnresponsive();

        code(() -> {
            http.get("/hello.html");
        }).should(throwException(Pattern.compile("Read timed out")));
        // mark-unresponsive

        myServer.fix();
    }

    @Test
    public void serverBreak() {
        // mark-broken
        myServer.markBroken();

        http.get("/hello.html", (header, body) -> {
            header.statusCode.should(equal(500));
        });
        // mark-broken

        myServer.fix();
    }

    // separate method for docs extraction purpose
    private static WebTauServer createServer() {
        // static-server-create
        WebTauServer myServer = server.serve("my-server", "src/test/resources/staticcontent");
        // static-server-create

        return myServer;
    }
}
