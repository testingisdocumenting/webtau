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


import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.datanode.DataNode
import org.testingisdocumenting.webtau.http.datanode.GroovyDataNode
import org.testingisdocumenting.webtau.http.validation.HeaderDataNode
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn

class GraphQLExtensions {
    static def execute(GraphQL graphQL, String query, Closure validation) {
        return graphQL.execute(query, closureToHttpResponseValidator(query, null, validation))
    }

    static def execute(GraphQL graphQL, String query, HttpHeader header, Closure validation) {
        return graphQL.execute(query, header, closureToHttpResponseValidator(query, null, validation))
    }

    static def execute(GraphQL graphQL, String query, String operationName, Closure validation) {
        return graphQL.execute(query, operationName, closureToHttpResponseValidator(query, operationName, validation))
    }

    static def execute(GraphQL graphQL, String query, String operationName, HttpHeader header, Closure validation) {
        return graphQL.execute(query, operationName, header, closureToHttpResponseValidator(query, operationName, validation))
    }

    static def execute(GraphQL graphQL, String query, Map<String, Object> variables, Closure validation) {
        return graphQL.execute(query, variables, closureToHttpResponseValidator(query, null, validation))
    }

    static def execute(GraphQL graphQL, String query, Map<String, Object> variables, HttpHeader header, Closure validation) {
        return graphQL.execute(query, variables, header, closureToHttpResponseValidator(query, null, validation))
    }

    static def execute(GraphQL graphQL, String query, Map<String, Object> variables, String operationName, Closure validation) {
        return graphQL.execute(query, variables, operationName, closureToHttpResponseValidator(query, operationName, validation))
    }

    static def execute(GraphQL graphQL, String query, Map<String, Object> variables, String operationName, HttpHeader header, Closure validation) {
        return graphQL.execute(query, variables, operationName, header, closureToHttpResponseValidator(query, operationName, validation))
    }

    private static HttpResponseValidatorWithReturn closureToHttpResponseValidator(String query, String operationName, validation) {
        return new HttpResponseValidatorWithReturn() {
            @Override
            def validate(final HeaderDataNode header, final DataNode body) {
                def cloned = validation.clone() as Closure
                cloned.delegate = new ValidatorDelegate(query, operationName, header, body)
                cloned.resolveStrategy = Closure.OWNER_FIRST
                return cloned.maximumNumberOfParameters == 2 ?
                    cloned.call(header, new GroovyDataNode(body)) :
                    cloned.call()
            }
        }
    }

    private static class ValidatorDelegate {
        private Set<GraphQLQuery> queries
        private HeaderDataNode header
        private DataNode body

        ValidatorDelegate(String query, String operationName, HeaderDataNode header, DataNode body) {
            this.queries = GraphQL.schema.findQueries(query, operationName)
            this.body = body
            this.header = header
        }

        def getProperty(String name) {
            switch (name) {
                case "header":
                    return new GroovyDataNode(header)
                case "body":
                    return new GroovyDataNode(body)
                case "errors":
                    return new GroovyDataNode(body).get("errors")
                case "data":
                    return new GroovyDataNode(body).get("data")
                default:
                    if (queries.size() != 1 || name == queries.first().name) {
                        return new GroovyDataNode(body).get("data").get(name)
                    } else {
                        return new GroovyDataNode(body).get("data").get(queries.first().name).get(name)
                    }
            }
        }
    }
}
