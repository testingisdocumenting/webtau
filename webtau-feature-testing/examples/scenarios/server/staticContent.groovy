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

package scenarios.server

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def expectedHtml = "<body>\n" +
        "<p>hello</p>\n" +
        "</body>"

scenario("static content server") {
    // static-server-create
    def myServer = server.serve("my-server", "data/staticcontent")
    // static-server-create

    http.get("http://localhost:${myServer.port}/hello.html") {
        body.should == expectedHtml
    }

    http.get("${myServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }

    // static-server-json
    http.get("${myServer.baseUrl}/data.json") {
        body.type == "person"
    }
    // static-server-json

    browser.open("${myServer.baseUrl}/hello.html")
    $("p").should == "hello"

    // set-base-url-example
    myServer.setAsBaseUrl()
    // set-base-url-example

    // static-server-html
    http.get("/hello.html") {
        body.should == expectedHtml
    }
    // static-server-html
}

scenario("auto generated server id") {
    def myServer = server.serve("data/staticcontent")
    http.get("${myServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("slow down") {
    def myServer = server.serve("my-server-slown-down", "data/staticcontent")
    myServer.setAsBaseUrl()

    // mark-unresponsive
    myServer.markUnresponsive()

    code {
        http.get("/hello.html")
    } should throwException(~/request timed out/)
    // mark-unresponsive

    myServer.fix()
    http.get("/hello.html") {
        body.should == expectedHtml
    }
}

scenario("broken") {
    def myServer = server.serve("my-server-broken", "data/staticcontent")
    myServer.setAsBaseUrl()

    // mark-broken
    myServer.markBroken()

    http.get("/hello.html") {
        statusCode.should == 500
        body.should == null
    }
    // mark-broken

    // mark-fix
    myServer.fix()
    // mark-fix

    http.get("/hello.html") {
        body.should == expectedHtml
    }
}

scenario("response override") {
    def myServer = server.serve("my-server-override", "data/staticcontent")
    myServer.setAsBaseUrl()

    // override-example
    def router = server.router()
            .get("/hello/:name") {request -> server.response([message: "hello ${request.param("name")}"]) }
    myServer.addOverride(router)
    // override-example

    http.get("/hello/world") {
        message.should == "hello world"
    }

    http.get("/hello.html") {
        body.should == expectedHtml
    }
}

scenario("same server id re-use is not allowed") {
    server.serve("my-server", "data/staticcontent")
}

scenario("non existing path") {
    server.serve("wrong-server", "wrong-path/staticcontent")
}