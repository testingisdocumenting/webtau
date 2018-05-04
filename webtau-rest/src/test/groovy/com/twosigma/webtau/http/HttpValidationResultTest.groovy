package com.twosigma.webtau.http

import com.twosigma.webtau.data.traceable.CheckLevel
import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import com.twosigma.webtau.utils.JsonUtils
import org.junit.Test

import static com.twosigma.webtau.Ddjt.equal

class HttpValidationResultTest {
    @Test
    void "should capture validation results"() {
        def responseAsMap = [childA: 'valueA', childB: 'valueB', childC: 'valueC']
        def responseAsJson = JsonUtils.serialize(responseAsMap)

        def n = DataNodeBuilder.fromMap(new DataNodeId('body'), responseAsMap)
        n.get('childA').get().updateCheckLevel(CheckLevel.FuzzyFailed)
        n.get('childB').get().updateCheckLevel(CheckLevel.ExplicitPassed)

        def validationResult = new HttpValidationResult('POST', '/test/url', 'http://site/test/url', null,
                new HttpResponse(content: responseAsJson, contentType: 'application/json', statusCode: 200),
                new HeaderDataNode(), n, 100)

        validationResult.toMap().should equal([method: 'POST', url: 'http://site/test/url', responseType: 'application/json',
                                               responseBody: responseAsJson,
                                               mismatches: [],
                                               responseStatusCode: 200,
                                               elapsedTime: 100,
                                               responseBodyChecks: [failedPaths: ['root.childA'], passedPaths:['root.childB']]])
    }
}
