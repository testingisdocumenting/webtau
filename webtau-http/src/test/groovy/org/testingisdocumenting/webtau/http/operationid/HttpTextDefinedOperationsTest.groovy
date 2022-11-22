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

package org.testingisdocumenting.webtau.http.operationid

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class HttpTextDefinedOperationsTest {
    @Test
    void "parse and find operation by url and method"() {
        def operations = new HttpTextDefinedOperations("""
PUT  /my/url/:id
POST    /theirs/:type/:id
""")
        def id = operations.findOperationId("POST", "http://site.org/theirs/machine/id2")
        id.should == "POST /theirs/:type/:id"
    }

    @Test
    void "validate incomplete lines"() {
        code {
            new HttpTextDefinedOperations(""" PUT     """)
        } should throwException("route line must be in format METHOD /path/:with/params/:id")
    }

    @Test
    void "validate method names"() {
        code {
            new HttpTextDefinedOperations(""" PAT /url/:id   """)
        } should throwException("unsupported method <PAT>, must be one of the following: GET, POST, PUT, PATCH, DELETE")
    }
}
