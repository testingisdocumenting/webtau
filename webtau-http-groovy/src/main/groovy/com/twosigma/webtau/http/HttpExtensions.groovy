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

package com.twosigma.webtau.http

import com.twosigma.webtau.http.datanode.DataNode
import com.twosigma.webtau.http.datanode.GroovyDataNode
import com.twosigma.webtau.http.json.JsonRequestBody
import com.twosigma.webtau.http.request.HttpQueryParams
import com.twosigma.webtau.http.request.HttpRequestBody
import com.twosigma.webtau.http.validation.HeaderDataNode
import com.twosigma.webtau.http.validation.HttpResponseValidatorWithReturn

class HttpExtensions {
    static def get(Http http, String url, Closure validation) {
        return http.get(url, closureToHttpResponseValidator(validation))
    }

    static def get(Http http, String url, HttpHeader header, Closure validation) {
        return http.get(url, header, closureToHttpResponseValidator(validation))
    }

    static def get(Http http, String url, Map<String, ?> queryParams, HttpHeader header, Closure validation) {
        return http.get(url, new HttpQueryParams(queryParams), header, closureToHttpResponseValidator(validation))
    }

    static def get(Http http, String url, Map<String, ?> queryParams, HttpHeader header) {
        return http.get(url, new HttpQueryParams(queryParams), header)
    }

    static def get(Http http, String url, HttpQueryParams queryParams, HttpHeader header, Closure validation) {
        return http.get(url, queryParams, header, closureToHttpResponseValidator(validation))
    }

    static def get(Http http, String url, Map<String, ?> queryParams, Closure validation) {
        return http.get(url, new HttpQueryParams(queryParams), closureToHttpResponseValidator(validation))
    }

    static def get(Http http, String url, Map<String, ?> queryParams) {
        return http.get(url, new HttpQueryParams(queryParams))
    }

    static def get(Http http, String url, HttpQueryParams queryParams, Closure validation) {
        return http.get(url, queryParams, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody, Closure validation) {
        return http.post(url, queryParams, header, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpHeader header, Map<String, Object> requestBody, Closure validation) {
        return http.post(url, header, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpQueryParams queryParams, Map<String, Object> requestBody, Closure validation) {
        return http.post(url, queryParams, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, Map<String, Object> requestBody, Closure validation) {
        return http.post(url, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpQueryParams queryParams, Closure validation) {
        return http.post(url, queryParams, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, Closure validation) {
        return http.post(url, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpQueryParams queryParams, HttpHeader header, Closure validation) {
        return http.post(url, queryParams, header, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpHeader header, Closure validation) {
        return http.post(url, header, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, Closure validation) {
        return http.post(url, queryParams, header, requestBody, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpHeader header, HttpRequestBody requestBody, Closure validation) {
        return http.post(url, header, requestBody, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpQueryParams queryParams, HttpRequestBody requestBody, Closure validation) {
        return http.post(url, queryParams, requestBody, closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, HttpRequestBody requestBody, Closure validation) {
        return http.post(url, requestBody, closureToHttpResponseValidator(validation))
    }

    static void post(Http http, String url, HttpQueryParams queryParams, Map<String, Object> requestBody) {
        http.post(url, queryParams, new JsonRequestBody(requestBody))
    }

    static void post(Http http, String url, Map<String, Object> requestBody) {
        http.post(url, new JsonRequestBody(requestBody))
    }

    static def put(Http http, String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, Closure validation) {
        return http.put(url, queryParams, header, requestBody, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody, Closure validation) {
        return http.put(url, queryParams, header, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpHeader header, Map<String, Object> requestBody, Closure validation) {
        return http.put(url, header, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpHeader header, HttpRequestBody requestBody, Closure validation) {
        return http.put(url, header, requestBody, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpQueryParams queryParams, HttpRequestBody requestBody, Closure validation) {
        return http.put(url, queryParams, requestBody, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpRequestBody requestBody, Closure validation) {
        return http.put(url, requestBody, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpQueryParams queryParams, Map<String, Object> requestBody, Closure validation) {
        return http.put(url, queryParams, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, Map<String, Object> requestBody, Closure validation) {
        return http.put(url, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpQueryParams queryParams, Closure validation) {
        return http.put(url, queryParams, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, Closure validation) {
        return http.put(url, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpQueryParams queryParams, HttpHeader header, Closure validation) {
        return http.put(url, queryParams, header, closureToHttpResponseValidator(validation))
    }

    static def put(Http http, String url, HttpHeader header, Closure validation) {
        return http.put(url, header, closureToHttpResponseValidator(validation))
    }

    static def delete(Http http, String url, Map<String, ?> queryParams, HttpHeader header, Closure validation) {
        return http.delete(url, new HttpQueryParams(queryParams), header, closureToHttpResponseValidator(validation))
    }

    static def delete(Http http, String url, HttpQueryParams queryParams, HttpHeader header, Closure validation) {
        return http.delete(url, queryParams, header, closureToHttpResponseValidator(validation))
    }

    static def delete(Http http, String url, Map<String, ?> queryParams, Closure validation) {
        return http.delete(url, new HttpQueryParams(queryParams), closureToHttpResponseValidator(validation))
    }

    static def delete(Http http, String url, HttpQueryParams queryParams, Closure validation) {
        return http.delete(url, queryParams, closureToHttpResponseValidator(validation))
    }

    static def delete(Http http, String url, HttpHeader header, Closure validation) {
        return http.delete(url, header, closureToHttpResponseValidator(validation))
    }

    static def delete(Http http, String url, Closure validation) {
        return http.delete(url, closureToHttpResponseValidator(validation))
    }

    private static HttpResponseValidatorWithReturn closureToHttpResponseValidator(validation) {
        return new HttpResponseValidatorWithReturn() {
            @Override
            def validate(final HeaderDataNode header, final DataNode body) {
                def cloned = validation.clone() as Closure
                cloned.delegate = new ValidatorDelegate(header, body)
                cloned.resolveStrategy = Closure.OWNER_FIRST
                return cloned.maximumNumberOfParameters == 2 ?
                        cloned.call(header, new GroovyDataNode(body)):
                        cloned.call()
            }
        }
    }

    private static class ValidatorDelegate {
        private HeaderDataNode header
        private DataNode body

        ValidatorDelegate(HeaderDataNode header, DataNode body) {
            this.body = body
            this.header = header
        }

        def getProperty(String name) {
            switch (name) {
                case "header":
                    return new GroovyDataNode(header)
                case "body":
                    return new GroovyDataNode(body)
                case "statusCode":
                    return new GroovyDataNode(header).get("statusCode")
                default:
                    return new GroovyDataNode(body).get(name)
            }
        }
    }
}
