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

package scenarios.cache

// example
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def createdId = cache.value("cli-heavy-created-id") // declare cached value with distinct id

scenario("heavy setup operation") {
    def cliResult = cli.run("scripts/cli-heavy-process") // long running process that you don't want to re-run as you write your tests
    createdId.set(cliResult.extractFromOutputByRegexp("id=(\\S+)")) // caching the extracted id from CLI run
}

scenario("using previous setup id even after restart") {
    def id = createdId.get() // using cached value from previous test run. value will be preserved between restarts and re-compile
    http.get("/resource/${id}") {
        message.should == "hello"
    }
}
// example

scenario("cache value presence") {
    // exists-example
    def prevGenerated = cache.value("zip-unpacked-path")
    // ...
    if (!prevGenerated.exists()) {
        // recreate content
    }
    // exists-example
}