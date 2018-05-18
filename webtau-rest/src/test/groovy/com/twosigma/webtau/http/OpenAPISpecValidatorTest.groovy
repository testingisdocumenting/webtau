package com.twosigma.webtau.http

import org.junit.Before
import org.junit.Test

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import static org.hamcrest.core.StringContains.containsString
import static org.junit.Assert.assertThat

class OpenAPISpecValidatorTest {
    private final static String GET = "GET"
    private final static String URL = "http://myhost.com:1234/"

    private OpenAPISpecValidator validator

    @Before
    void setUp() throws Exception {
        def specUrl = this.class.classLoader.getResource("test-spec.json")
        validator = new OpenAPISpecValidator(specUrl.toString())
    }

    @Test
    void "invalid responses should generate mismatches"() {
        //This should generate two errors: one for missing mandatoryField and one for invalid type of intField.
        //It should generate no errors for the missing optionalField
        def testResponse = "{\"intField\": \"abc\"}"
        def response = ok(testResponse)
        def result = emptyResult()

        validator.validateApiSpec(GET, URL, response, result)

        result.mismatches.size().should == 2
        assertThat(result.mismatches, containsInAnyOrder(
            containsString("does not match any allowed primitive type"),
            containsString("missing required properties")
        ))
    }

    @Test
    void "valid response generates no mismatches"() {
        def testResponse = "{\"mandatoryField\": \"foo\"}"
        def response = ok(testResponse)
        def result = emptyResult()

        validator.validateApiSpec(GET, URL, response, result)

        result.mismatches.size().should == 0
    }

    static HttpResponse ok(String body) {
        def response = new HttpResponse()
        response.setContent(body)
        response.setStatusCode(200)

        return response
    }

    static HttpValidationResult emptyResult() {
        return new HttpValidationResult(null, null, null, null, null, null, null, 0)
    }
}
