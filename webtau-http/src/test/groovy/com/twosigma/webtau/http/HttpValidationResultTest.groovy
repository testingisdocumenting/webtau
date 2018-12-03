/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http

import com.twosigma.webtau.data.traceable.CheckLevel
import com.twosigma.webtau.data.traceable.TraceableValue
import com.twosigma.webtau.http.binary.BinaryRequestBody
import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import com.twosigma.webtau.http.datanode.StructuredDataNode
import com.twosigma.webtau.http.validation.HeaderDataNode
import com.twosigma.webtau.http.validation.HttpValidationResult
import com.twosigma.webtau.utils.JsonUtils
import org.junit.Test

class HttpValidationResultTest {
    private static commonExpectation = [
            id: ~/httpCall-\d+/,
            method: 'POST',
            url: 'http://site/test/url',
            requestHeader: [],
            responseHeader: [],
            errorMessage: null,
            responseStatusCode: 200,
            mismatches: [],
            startTime: 12345678,
            elapsedTime: 100]

    @Test
    void "should capture validation results"() {
        def responseAsMap = [childA: 'valueA', childB: 'valueB', childC: 'valueC']
        def responseAsJson = JsonUtils.serialize(responseAsMap)

        def n = DataNodeBuilder.fromMap(new DataNodeId('body'), responseAsMap)
        n.get('childA').get().updateCheckLevel(CheckLevel.FuzzyFailed)
        n.get('childB').get().updateCheckLevel(CheckLevel.ExplicitPassed)

        def validationResult = createValidationResult(null)
        validationResult.setResponse(new HttpResponse(textContent: responseAsJson, contentType: 'application/json', statusCode: 200))
        validationResult.setResponseBodyNode(n)

        validationResult.toMap().should == [
                *: commonExpectation,
                responseType: 'application/json',
                responseBody: responseAsJson,
                responseBodyChecks: [failedPaths: ['root.childA'], passedPaths:['root.childB']]]
    }

    @Test
    void "should replace binary content with placeholder"() {
        def binaryContent = [1, 2, 3] as byte[]
        def binaryNode = new StructuredDataNode(new DataNodeId('body'), new TraceableValue(binaryContent))

        def validationResult = createValidationResult(BinaryRequestBody.withType('application/octet-stream', binaryContent))

        validationResult.setResponse(new HttpResponse(binaryContent: binaryContent, contentType: 'application/octet-stream', statusCode: 200))
        validationResult.setResponseBodyNode(binaryNode)

        validationResult.toMap().should == [
                *: commonExpectation,
                requestType: 'application/octet-stream',
                requestBody: '[binary content]',
                responseType: 'application/octet-stream',
                responseBody: '[binary content]',
                responseBodyChecks: [failedPaths: [], passedPaths: []]]
    }

    private static HttpValidationResult createValidationResult(requestBody) {
        def validationResult = new HttpValidationResult('POST', 'http://site/test/url',
                new HttpHeader([:]), requestBody)

        validationResult.setStartTime(12345678)
        validationResult.setElapsedTime(100)
        validationResult.setResponseHeaderNode(new HeaderDataNode(new HttpResponse()))

        return validationResult
    }
}
