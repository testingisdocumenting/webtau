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

def targetServer = server.serve("content-to-proxy", "data/staticcontent")

def expectedHtml = "<body>\n" +
        "<p>hello</p>\n" +
        "</body>"

scenario("proxy server") {
    // proxy-server-creation
    def proxyServer = server.proxy("test-proxy-server", targetServer.baseUrl)
    // proxy-server-creation

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("proxy server of fake server") {
    def fakeServer = server.fake("fake-server",
            server.router().put("/hello/:name", {request ->
                server.response(200, [greeting: "hello ${request.param("name")}"])
            }))

    def proxyServer = server.proxy("test-proxy-fake-server", fakeServer.baseUrl, 0)

    http.put("${proxyServer.baseUrl}/hello/world", [message: "welcome"]) {
        greeting.should == "hello world"
    }
}

scenario("slowed down proxy") {
    def proxyServer = server.proxy("slow-proxy-server", targetServer.baseUrl)
    // mark-unresponsive-example
    proxyServer.markUnresponsive()
    // mark-unresponsive-example

    code {
        http.get("${proxyServer.baseUrl}/hello.html") {
            body.should == expectedHtml
        }
    } should throwException(~/Read timed out/)

    proxyServer.fix()

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("broken proxy") {
    def proxyServer = server.proxy("broken-proxy-server", targetServer.baseUrl)
    // mark-broken
    proxyServer.markBroken()
    // mark-broken

    // mark-broken-response
    http.get("${proxyServer.baseUrl}/hello.html") {
        statusCode.should == 500
        body.should == null
    }
    // mark-broken-response

    proxyServer.fix()
}

scenario("proxy override") {
    def proxyServer = server.proxy("proxy-server-with-override", targetServer.baseUrl)

    // proxy-add-override
    def router = server.router("optional-router-id")
    router.get("/another/{id}", (request) -> server.response([anotherId: request.param("id")]))

    proxyServer.addOverride(router)
    // proxy-add-override

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }

    http.get("${proxyServer.baseUrl}/another/hello") {
        body.should == [anotherId: "hello"]
    }

    router.get("/hello.html", (request) -> server.response([id: 'test']))

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == [id: "test"]
    }
}

scenario("proxy override and slow down") {
    def proxyServer = server.proxy("proxy-server-with-override-and-slowdown", targetServer.baseUrl)

    def routerA = server.router("__overrides-a")
    routerA.get("/another/{id}", (request) -> server.response([anotherId: request.param("id")]))
    def routerB = server.router("overrides-b")
    routerB.get("/hello/{id}", (request) -> server.response([hello: request.param("id")]))

    proxyServer.addOverride(routerA)
    // mark-unresponsive
    proxyServer.markUnresponsive()
    // mark-unresponsive
    proxyServer.addOverride(routerB)

    // unresponsive-time-out-throw
    code {
        http.get("${proxyServer.baseUrl}/another/hello") {
            body.should == [anotherId: "hello"]
        }
    } should throwException(~/Read timed out/)
    // unresponsive-time-out-throw

    // mark-fix
    proxyServer.fix()
    // mark-fix

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }

    http.get("${proxyServer.baseUrl}/another/hello") {
        body.should == [anotherId: "hello"]
    }
}

scenario("proxy override response and make original call") {
    def receivedHeader = [:]
    def receivedContent = [:]
    def serverToProxy = server.fake("counting-server",
            server.router().post("/hello", { request ->
                receivedHeader.putAll(request.header)
                receivedContent.putAll(request.contentAsMap)

                return server.response(201, [message: "hello world"])
            }))

    def capturedMessages = []
    def proxyServer = server.proxy("with-original-call", serverToProxy.baseUrl)
    // override-with-original-call
    def router = server.router().post("/hello", { request ->
        def message = http.post(http.concatUrl(proxyServer.urlToProxy, request.uri),
                http.header(request.header), request.contentAsMap) {
            return body.message
        }

        // optional logic with original response
        capturedMessages << message

        return server.statusCode(500)
    })

    proxyServer.addOverride(router)
    // override-with-original-call

    http.post(http.concatUrl(proxyServer.baseUrl, "/hello"),
            http.header(["x-something": "b123"]),
            [postBody: "100"]) {
        statusCode.should == 500
    }

    receivedHeader["x-something"].should == "b123"
    receivedContent.should == [postBody: "100"]
    capturedMessages.should == ["hello world"]
}
