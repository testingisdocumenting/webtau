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
// path-param-example
        .get("/hello/:name") { request ->
            server.response([message: "hello " +
                    request.param("name")])
        }
// path-param-example
// query-param-example
        .get("/bye/:name") { request ->
            server.response([message: "bye " +
                    request.queryParam("title") +
                    " " + request.param("name")])
        }
// query-param-example

scenario("properties check") {
    def myServer = server.fake("properties-check-server", router)

    http.get("${myServer.baseUrl}/hello/person") {
        message.should == "hello person"
    }

    http.get("${myServer.baseUrl}/bye/person?title=X") {
        message.should == "bye X person"
    }
}
