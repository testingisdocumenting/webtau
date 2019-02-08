/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.utils

import org.junit.Test

class JsonUtilsTest {
    @Test
    void "should serialize json using compact print"() {
        def asText = JsonUtils.serialize([k1: "v1", k2: ["v2", "v3"]])
        assert asText == '{"k1":"v1","k2":["v2","v3"]}'
    }

    @Test
    void "should serialize json using pretty print"() {
        def asText = JsonUtils.serializePrettyPrint([k1: "v1", k2: ["v2", "v3"]])

        assert asText == '{\n' +
            '  "k1" : "v1",\n' +
            '  "k2" : [ "v2", "v3" ]\n' +
            '}'
    }

    @Test
    void "should deserialize json as map"() {
        def map = JsonUtils.deserializeAsMap("""{
              "id": 10,
              "value": [
                "v2",
                "v3"
              ]
            }""")

        assert map.id.getClass() == Integer
        assert map.id == 10
        assert map.value == ['v2', 'v3']
    }

    @Test
    void "should deserialize json as list"() {
        def json = """[{"name": "hello"}, {"name": "world"}] """

        def list = JsonUtils.deserializeAsList(json)
        assert list == [[name: "hello"], [name: "world"]]
    }

    @Test(expected = JsonParseException)
    void "should report deserialize error"() {
        def json = """[{"name": "hello", {"name": "world"}] """
        JsonUtils.deserializeAsList(json)
    }

    @Test
    void "should deserialize json as object"() {
        def listJson = """["hello", "world"] """

        def list = JsonUtils.deserialize(listJson)
        assert list instanceof List

        def mapJson = """{
    "hello": "world",
    "another": {"nested": "value"}} """
        def map = JsonUtils.deserialize(mapJson)
        assert map instanceof Map
    }

    @Test
    void "should serialize null as null"() {
        def asText = JsonUtils.serialize(null)

        assert asText == null
    }

    @Test
    void "should pretty serialize null as null"() {
        def asText = JsonUtils.serializePrettyPrint(null)

        assert asText == null
    }

    @Test
    void "should deserialize null to map"() {
        def map = JsonUtils.deserializeAsMap(null)

        assert map == null
    }

    @Test
    void "should deserialize null to list"() {
        def list = JsonUtils.deserializeAsList(null)

        assert list == null
    }

    @Test
    void "should deserialize null to object"() {
        def obj = JsonUtils.deserialize(null)

        assert obj == null
    }
}
