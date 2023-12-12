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

// router-example
def router = server.router()
        .get("/hello/:name") { request -> server.response([message: "hello ${request.param("name")}"]) }
        .get("/bye/:name") { request -> server.response([message: "bye ${request.param("name")}"]) }
// router-example

scenario("fake rest server") {
    // server-create-example
    def myServer = server.fake("my-rest-server", router)
    // server-create-example

    // fake-response-check
    http.get("${myServer.baseUrl}/hello/person") {
        message.should == "hello person"
    }

    http.get("${myServer.baseUrl}/bye/person") {
        message.should == "bye person"
    }
    // fake-response-check
}

scenario("auto generated server id") {
    def myServer = server.fake(router)
    http.get("${myServer.baseUrl}/hello/person") {
        message.should == "hello person"
    }
}

scenario("slow down") {
    def myServer = server.fake("my-rest-server-slowed-down", router)

    // mark-unresponsive
    myServer.markUnresponsive()

    code {
        http.get("${myServer.baseUrl}/hello/person") {
            message.should == "hello person"
        }
    } should throwException(~/request timed out/)
    // mark-unresponsive

    myServer.fix()
    http.get("${myServer.baseUrl}/hello/person") {
        message.should == "hello person"
    }
}

scenario("broken") {
    def myServer = server.fake("my-rest-server-broken", router)

    // mark-broken
    myServer.markBroken()

    http.get("${myServer.baseUrl}/hello/person") {
        statusCode.should == 500
        body.should == null
    }
    // mark-broken

    // mark-fix
    myServer.fix()

    http.get("${myServer.baseUrl}/hello/person") {
        message.should == "hello person"
    }
    // mark-fix
}