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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.Matchers.actual;
import static org.testingisdocumenting.webtau.Matchers.code;
import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.Matchers.throwException;
import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql;
import static org.testingisdocumenting.webtau.utils.CollectionUtils.aMapOf;

public class GraphQLJavaTest extends GraphQLTestBase {
    @Test
    public void execute() {
        String query = "query {" +
                "    allTasks(uncompletedOnly: false) {" +
                "        id" +
                "        description" +
                "    }" +
                "}";

        List<String> expectedIds = Arrays.asList("a", "b", "c");

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
        Map<String, Object> variables = aMapOf("id", id);
        graphql.execute(query, variables, (header, body) -> {
            body.get("errors").should(equal(null));
            body.get("data.taskById.id").should(equal(id));

            // We can remove this once we have overrides which take HttpResponseValidatorIgnoringReturn
            return null;
        });
    }

    @Test
    public void executeQueryWhichGeneratesErrors() {
        String query = "{ generateError { id } }";
        graphql.execute(query, (header, body) -> {
            body.get("errors").shouldNot(equal(null));
        });
    }

    @Test
    public void implicitErrorsCheck() {
        String query = "{ generateError { id } }";
        code(() -> {
            graphql.execute(query, (header, body) -> {
                body.get("data.generateError").should(equal(null));
            });
        }).should(throwException(AssertionError.class, Pattern.compile("body.errors:   actual: \\[\\{")));
    }
}
