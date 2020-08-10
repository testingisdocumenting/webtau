/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.graphql

import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.http.request.HttpRequestBody
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult

class TestUtils {
    static HttpValidationResult validationResult(operationName, operationType, elapsedTime = 0) {
        def response = new HttpResponse()
        response.statusCode = 200

        def result = new HttpValidationResult('POST', '/graphql', '/graphql', null, body(operationName, operationType))
        result.setResponse(response)
        result.setElapsedTime(elapsedTime)
        return result
    }

    static HttpRequestBody body(operationName, operationType) {
        def type = operationType.name().toLowerCase()
        def payload = [
            query: """
                    $type {
                        $operationName {
                            id
                        }
                    }
                    """.toString()
        ]
        return new JsonRequestBody(payload)
    }
}
