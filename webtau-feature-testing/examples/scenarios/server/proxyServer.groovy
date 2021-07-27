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

def staticServer = server.serve("content-to-proxy", "data/staticcontent")

def expectedHtml = "<body>\n" +
        "<p>hello</p>\n" +
        "</body>"

scenario("proxy server") {
    def proxyServer = server.proxy("test-proxy-server", staticServer.baseUrl, 0)

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("slowed down proxy") {
    def slowDownServer = server.proxy("slow-proxy-server", staticServer.baseUrl)
    slowDownServer.markUnresponsive()

    code {
        http.get("${slowDownServer.baseUrl}/hello.html") {
            body.should == expectedHtml
        }
    } should throwException(~/Read timed out/)

    slowDownServer.fix()

    http.get("${slowDownServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }
}

scenario("broken proxy") {
    def brokenServer = server.proxy("broken-proxy-server", staticServer.baseUrl)
    brokenServer.markBroken()

    http.get("${brokenServer.baseUrl}/hello.html") {
        statusCode.should == 500
        body.should == null
    }

    brokenServer.fix()
}

scenario("proxy override") {
    def proxyServer = server.proxy("proxy-server-with-override", staticServer.baseUrl)

    def router = server.router("overrides")
    router.get("/another/{id}", (request) -> server.response([anotherId: request.param("id")]))
    proxyServer.addOverride(router)

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
    def proxyServer = server.proxy("proxy-server-with-override-and-slowdown", staticServer.baseUrl)

    def routerA = server.router("__overrides-a")
    routerA.get("/another/{id}", (request) -> server.response([anotherId: request.param("id")]))
    def routerB = server.router("overrides-b")
    routerB.get("/hello/{id}", (request) -> server.response([hello: request.param("id")]))

    proxyServer.addOverride(routerA)
    proxyServer.markUnresponsive()
    proxyServer.addOverride(routerB)

    code {
        http.get("${proxyServer.baseUrl}/another/hello") {
            body.should == [anotherId: "hello"]
        }
    } should throwException(~/Read timed out/)

    proxyServer.fix()

    http.get("${proxyServer.baseUrl}/hello.html") {
        body.should == expectedHtml
    }

    http.get("${proxyServer.baseUrl}/another/hello") {
        body.should == [anotherId: "hello"]
    }
}
