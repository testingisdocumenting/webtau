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

package org.testingisdocumenting.webtau.graphql;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql;

public class GraphQLJavaTest extends GraphQLTestBase {
    @Test
    public void execute() {
        String query = "query {" +
                "    allTasks(uncompletedOnly: false) {" +
                "        id" +
                "        description" +
                "    }" +
                "}";

        List<String> expectedIds = listOf("a", "b", "c");

        List<String> ids = graphql.execute(query, (header, body) -> {
            body.get("errors").should(equal(null));
            body.get("data.allTasks.id").should(equal(expectedIds));
            return body.get("data.allTasks.id");
        });

        actual(ids).should(equal(expectedIds));
    }

    @Test
    public void executeWithVariables() {
        String query = "query taskById($id: ID!) {" +
                "    taskById(id: $id) {" +
                "        id" +
                "        description" +
                "        completed" +
                "    }" +
                "}";

        String id = "a";
        Map<String, Object> variables = mapOf("id", id);
        graphql.execute(query, variables, (header, body) -> {
            body.get("errors").should(equal(null));
            body.get("data.taskById.id").should(equal(id));
        });
    }

    @Test
    public void explicitStatusCodeCheck() {
        int successStatusCode = 201;
        testServer.getHandler().withSuccessStatusCode(successStatusCode, () -> {
            graphql.execute("{ allTasks { id }  }", (header, body) -> {
                header.statusCode.should(equal(successStatusCode));
            });
        });
    }
}
