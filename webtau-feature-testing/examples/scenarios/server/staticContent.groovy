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

scenario("static content server") {
    def server = server.serve("my-server", "data/staticcontent")

    def expected = "<body>\n" +
            "<p>hello</p>\n" +
            "</body>"

    http.get("http://localhost:${server.port}/hello.html") {
        body.should == expected
    }

    http.get("${server.baseUrl}/hello.html") {
        body.should == expected
    }

    server.setAsBaseUrl()
    http.get("/hello.html") {
        body.should == expected
    }
}

scenario("same server id re-use is not allowed") {
    server.serve("my-server", "data/staticcontent")
}

scenario("non existing path") {
    server.serve("wrong-server", "wrong-path/staticcontent")
}