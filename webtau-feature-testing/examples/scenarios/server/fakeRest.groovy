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

def router = server.router()
        .get("/hello/:name") { request -> server.response([message: "hello ${request.param("name")}"]) }
        .get("/bye/:name") { request -> server.response([message: "bye ${request.param("name")}"]) }

scenario("fake rest server") {
    def server = server.fake("my-rest-server", router)

    http.get("${server.baseUrl}/hello/person") {
        message.should == "hello person"
    }

    http.get("${server.baseUrl}/bye/person") {
        message.should == "bye person"
    }
}

scenario("slow down") {
    def server = server.fake("my-rest-server-slowed-down", router)

    server.markUnresponsive()
    code {
        http.get("${server.baseUrl}/hello/person") {
            message.should == "hello person"
        }
    } should throwException(~/Read timed out/)

    server.fix()
    http.get("${server.baseUrl}/hello/person") {
        message.should == "hello person"
    }
}

scenario("broken") {
    def server = server.fake("my-rest-server-broken", router)

    server.markBroken()
    http.get("${server.baseUrl}/hello/person") {
        statusCode.should == 500
        body.should == null
    }

    server.fix()
    http.get("${server.baseUrl}/hello/person") {
        message.should == "hello person"
    }
}