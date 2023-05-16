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

package scenarios.rest.proxy

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("use proxy value") {
    code {
        http.get("http://localhost:8080/hello.html") {
        }
    } should throwException(~/Caused by: java.nio.channels.UnresolvedAddressException/)

    code {
        http.get("https://localhost:8080/hello.html") {
        }
    } should throwException(~/Caused by: java.nio.channels.UnresolvedAddressException/)
}