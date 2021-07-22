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
    def staticServer = server.serve("my-server", "data/staticcontent")

    http.get("http://localhost:${staticServer.port}/hello.html") {
        body.should == expectedHtml
    }

    http.get("${staticServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }

    staticServer.setAsBaseUrl()
    http.get("/hello.html") {
        body.should == expectedHtml
    }
}

scenario("slow down") {
    def staticServer = server.serve("my-server-slown-down", "data/staticcontent")

    staticServer.markUnresponsive()
    code {
        http.get("${staticServer.baseUrl}/hello.html")
    } should throwException(~/Read timed out/)

    staticServer.fix()
    http.get("${staticServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("broken") {
    def staticServer = server.serve("my-server-broken", "data/staticcontent")

    staticServer.markBroken()
    http.get("${staticServer.baseUrl}/hello.html") {
        statusCode.should == 500
        body.should == null
    }

    staticServer.fix()
    http.get("${staticServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("response override") {
    def staticServer = server.serve("my-server-override", "data/staticcontent")

    def router = server.router().get("/hello/:name") {[message: "hello $it.name"] }
    staticServer.addOverride(router)

    http.get("${staticServer.baseUrl}/hello/world") {
        message.should == "hello world"
    }

    http.get("${staticServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("same server id re-use is not allowed") {
    server.serve("my-server", "data/staticcontent")
}

scenario("non existing path") {
    server.serve("wrong-server", "wrong-path/staticcontent")
}