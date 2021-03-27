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

package org.testingisdocumenting.webtau.http

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.http.Http.http

class HttpOutputTest extends HttpTestBase implements ConsoleOutput {
    String output

    @Before
    void init() {
        ConsoleOutputs.add(this)
        output = ""
    }

    @Before
    void cleanup() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "should print json request body"() {
        http.post("/echo", [hello: "world", id: "generated-id"])
        output.should contain("""  request (application/json):
  {
    "hello": "world",
    "id": "generated-id"
  }""")
    }

    @Test
    void "should print empty request body"() {
        http.post("/echo")
        output.should contain("  [no request body]")
    }

    @Test
    void "should print binary request body"() {
        http.post("/echo", http.application.octetStream([1, 2, 3] as byte[]))
        output.should contain("  [binary request]")
    }

    @Test
    void "should print text request body"() {
        http.post("/echo", http.text.plain("hello world"))
        output.should contain("  request (text/plain):\n" +
                "  hello world")
    }

    @Override
    void out(Object... styleOrValues) {
        output += new IgnoreAnsiString(styleOrValues).toString() + '\n'
    }

    @Override
    void err(Object... styleOrValues) {

    }
}
