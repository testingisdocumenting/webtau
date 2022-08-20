/*
 * Copyright 2022 webtau maintainers
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

package scenarios.cache

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def authTokenKey = "auth-token-cache-key"

scenario("auth token usage") {
    // example
    def token = cache.get(authTokenKey, 60_000, () -> generateAuthToken())
    // example
    trace "cached token", [token: token]
}

scenario("another auth token usage") {
    def token = cache.get(authTokenKey, 60_000, () -> generateAuthToken())
    trace "cached token", [token: token]
}

scenario("auth token usage after expiration") {
    sleep 10
    def token = cache.get(authTokenKey, 9, () -> generateAuthToken())
    trace "cached token", [token: token]
}

scenario("remove token") {
    cache.remove(authTokenKey)
}

def generateAuthToken() {
    return step("token generation") {
        return "my-auth-token-with-expiration"
    }
}
