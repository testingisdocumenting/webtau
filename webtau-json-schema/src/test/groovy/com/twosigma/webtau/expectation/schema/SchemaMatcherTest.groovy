package com.twosigma.webtau.expectation.schema

import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import com.twosigma.webtau.schema.expectation.SchemaMatcher
import org.junit.Test

import static com.twosigma.webtau.WebTauCore.*

class SchemaMatcherTest {
    private final static TEST_SCHEMA = "test-schema.json"

    @Test
    void "should pass when object matches schema"() {
        actual([name: "test", val: 123]).should(complyWithSchema(TEST_SCHEMA))
    }

    @Test
    void "should pass when data node matches schema"() {
        def dataNode = DataNodeBuilder.fromMap(new DataNodeId("test"), [name: "test", val: 123])
        actual(dataNode).should(complyWithSchema(TEST_SCHEMA))
    }

    @Test
    void "should throw exception when object does not match schema"() {
        code {
            actual([name: "test"]).should(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expected to comply with schema test-schema.json\n' +
            '[$.val: is missing but it is required]')
    }

    @Test
    void "should throw exception when object has incorrect types"() {
        code {
            actual([name: "test", val: "foo"]).should(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expected to comply with schema test-schema.json\n' +
            '[$.val: string found, integer expected]')
    }

    @Test
    void "should pass when object does not match expected schema and should not"() {
        actual([name: "test", val: "foo"]).shouldNot(complyWithSchema(TEST_SCHEMA))
    }

    @Test
    void "should throw exception when object matches schema but should not"() {
        code {
            actual([name: "test", val: 123]).shouldNot(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expected to not comply with schema test-schema.json\n' +
            '[]')
    }

    static SchemaMatcher complyWithSchema(String schemaFileName) {
        return new SchemaMatcher(schemaFileName)
    }
}
