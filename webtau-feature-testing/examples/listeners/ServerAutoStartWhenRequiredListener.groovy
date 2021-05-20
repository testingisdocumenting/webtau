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

package listeners

import org.testingisdocumenting.webtau.TestListener

import static org.testingisdocumenting.webtau.WebTauDsl.*

class ServerAutoStartWhenRequiredListener implements TestListener {
    @Override
    void beforeFirstTest() {
        if (http.ping("/")) {
            return
        }

        cfg.baseUrl = startServerAndGetBaseUrl()
    }

    static String startServerAndGetBaseUrl() {
        def server = new TestServer()
        server.start()

        return server.baseUrl
    }
}
