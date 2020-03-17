package org.testingisdocumenting.webtau.expectation.schema

import org.testingisdocumenting.webtau.http.datanode.DataNodeBuilder
import org.testingisdocumenting.webtau.http.datanode.DataNodeId
import org.testingisdocumenting.webtau.schema.expectation.SchemaMatcher
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*

class SchemaMatcherTest {
    private final static TEST_SCHEMA = "test-schema-default-version.json"
    private final static TEST_SCHEMA_v7 = "test-schema-v7.json"

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
        } should throwException('\n[value] expected to comply with schema test-schema-default-version.json\n' +
            '[$.val: is missing but it is required]')
    }

    @Test
    void "should throw exception when object has incorrect types"() {
        code {
            actual([name: "test", val: "foo"]).should(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expected to comply with schema test-schema-default-version.json\n' +
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
        } should throwException('\n[value] expected to not comply with schema test-schema-default-version.json\n' +
            '[]')
    }

    @Test
    void "should pass with older schema version"() {
        actual([name: "test", val: 123]).should(complyWithSchema(TEST_SCHEMA_v7))
    }

    static SchemaMatcher complyWithSchema(String schemaFileName) {
        return new SchemaMatcher(schemaFileName)
    }
}
