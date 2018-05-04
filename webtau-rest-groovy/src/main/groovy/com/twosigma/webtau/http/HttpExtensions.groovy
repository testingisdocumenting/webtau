package com.twosigma.webtau.http

import com.twosigma.webtau.http.datanode.DataNode
import com.twosigma.webtau.http.datanode.GroovyDataNode
import com.twosigma.webtau.http.json.JsonRequestBody

class HttpExtensions {
    static def get(Http http, String url, Closure validation) {
        return http.get(url, closureToHttpResponseValidator(validation))
    }

    static def get(Http http, String url, Map<String, ?> queryParams, Closure validation) {
        return http.get(url, new HttpQueryParams(queryParams), closureToHttpResponseValidator(validation))
    }

    static def post(Http http, String url, Map<String, ?> requestBody, Closure validation) {
        return http.post(url, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
    }

    static void post(Http http, String url, Map<String, ?> requestBody) {
        http.post(url, new JsonRequestBody(requestBody))
    }

    static def put(Http http, String url, Map<String, ?> requestBody, Closure validation) {
        return http.put(url, new JsonRequestBody(requestBody), closureToHttpResponseValidator(validation))
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
                cloned.resolveStrategy = Closure.DELEGATE_FIRST
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
