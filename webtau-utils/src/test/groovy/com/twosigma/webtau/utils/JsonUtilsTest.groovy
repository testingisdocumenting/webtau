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
                '  "k1": "v1",\n' +
                '  "k2": [\n' +
                '    "v2",\n' +
                '    "v3"\n' +
                '  ]\n' +
                '}'
    }

    @Test
    void "should deserialize json as map"() {
        def json = """{
    "hello": "world",
    "another": {"nested": "value"}} """

        def map = JsonUtils.deserializeAsMap(json)
        assert map == [hello: "world", another: [nested: "value"]]
    }

    @Test
    void "should deserialize json as list"() {
        def json = """["hello", "world"] """

        def list = JsonUtils.deserializeAsList(json)
        assert list == ["hello", "world"]
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
}
