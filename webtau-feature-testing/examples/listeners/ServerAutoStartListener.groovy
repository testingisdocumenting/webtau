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

import org.testingisdocumenting.webtau.cli.CliBackgroundCommand
import org.testingisdocumenting.webtau.reporter.TestListener
import org.testingisdocumenting.webtau.utils.RegexpUtils

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

class ServerAutoStartListener implements TestListener {
    CliBackgroundCommand server

    @Override
    void beforeFirstTest() {
        server = cli.runInBackground("java -jar ../webtau-testapp/target/springboot-testapp.jar --server.port=0")
        server.output.waitTo(contain("Tomcat started on port(s)"), 10_000)

        def port = RegexpUtils.extractByRegexp(server.output.get(), /Tomcat started on port\(s\): (\d+)/)
        println "@@@port:" + port
        cfg.baseUrl = "http://localhost:${port}"
    }

    @Override
    void afterAllTests() {
        if (server) {
            server.stop()
        }
    }
}
