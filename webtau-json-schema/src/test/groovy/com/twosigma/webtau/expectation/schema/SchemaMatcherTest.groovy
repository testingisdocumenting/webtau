package com.twosigma.webtau.expectation.schema

import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

class SchemaMatcherTest {
    private final static TEST_SCHEMA = "test-schema.json"

    @Test
    void "should pass when object matches schema"() {
        actual([name: "test", val: 123]).should(complyWithSchema(TEST_SCHEMA))
    }

    @Test
    void "should throw exception when object does not match schema"() {
        code {
            actual([name: "test"]).should(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expect to match schema test-schema.json\n' +
            '[#: required key [val] not found]')
    }

    @Test
    void "should throw exception when object has incorrect types"() {
        code {
            actual([name: "test", val: "foo"]).should(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expect to match schema test-schema.json\n' +
            '[#/val: expected type: Number, found: String]')
    }

    @Test
    void "should pass when object does not match expected schema and should not"() {
        actual([name: "test", val: "foo"]).shouldNot(complyWithSchema(TEST_SCHEMA))
    }

    @Test
    void "should throw exception when object matches schema but should not"() {
        code {
            actual([name: "test", val: 123]).shouldNot(complyWithSchema(TEST_SCHEMA))
        } should throwException('\n[value] expect to not match schema test-schema.json\n' +
            '[]')
    }

    static SchemaMatcher complyWithSchema(String schemaFileName) {
        return new SchemaMatcher(schemaFileName)
    }
}
