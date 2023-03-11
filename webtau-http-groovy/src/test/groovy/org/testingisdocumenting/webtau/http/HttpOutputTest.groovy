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


import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.http.Http.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class HttpOutputTest extends HttpTestBase {
    @Test
    void "should print json request body as multiline"() {
        runAndValidateOutput(contain("""  request (application/json):
  {
    "hello": ["world", "of"],
    "id": "generated-id"
  }""")) {
            http.post("/echo", [hello: ["world", "of"], id: "generated-id"])
        }
    }

    @Test
    void "should print json response body as multiline"() {
        runAndValidateOutput(contain("""  response (application/json):
  {
    "hello": ["world", "of"],
    "id": "generated-id"
  }""")) {
            http.post("/echo", [hello: ["world", "of"], id: "generated-id"])
        }
    }

    @Test
    void "should print empty request body"() {
        runAndValidateOutput(contain("  [no request body]")) {
            http.post("/echo")
        }
    }

    @Test
    void "should print binary request body"() {
        runAndValidateOutput(contain("  [binary request]")) {
            http.post("/echo", http.application.octetStream([1, 2, 3] as byte[]))
        }
    }

    @Test
    void "should print text request body"() {
        runAndValidateOutput(contain("  request (text/plain):\n" +
                "  hello world")) {
            http.post("/echo", http.text.plain("hello world"))
        }
    }
}
