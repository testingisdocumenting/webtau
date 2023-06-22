/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.server

import org.junit.Test

class QueryParamsParserTest {
    @Test
    void "no values"() {
        def params = QueryParamsParser.parse("")
        params.should == [:]
    }

    @Test
    void "incomplete values"() {
        def params = QueryParamsParser.parse("key")
        params.should == [:]
    }

    @Test
    void "single value"() {
        def params = QueryParamsParser.parse("key1=1&key2=two")
        params.should == [key1: [1], key2: ["two"]]
    }

    @Test
    void "decoded value"() {
        def params = QueryParamsParser.parse("key1=1&key2=%3D%3D%3D")
        params.should == [key1: [1], key2: ["==="]]
    }

    @Test
    void "multiple values"() {
        def params = QueryParamsParser.parse("key1=1&key1=one")
        params.should == [key1: [1, "one"]]
    }
}
