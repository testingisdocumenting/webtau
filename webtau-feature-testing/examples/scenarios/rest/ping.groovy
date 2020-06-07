/*
 * Copyright 2020 webtau maintainers
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

package scenarios.rest

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static org.testingisdocumenting.webtau.http.Http.http

scenario('ping') {
    if (!http.ping("/weather")) {
        http.post("/cluster-master", [restart: "weather"])
    }
}

scenario('ping failed') {
    http.ping("htp://non-existing-wrong-host/")
}

scenario('ping overloads') {
    http.ping("/weather")
    http.ping("/weather", ["query-param": "value"])
    http.ping("/weather", ["query-param": "value"], http.header(["X-flag": "test"]))
}