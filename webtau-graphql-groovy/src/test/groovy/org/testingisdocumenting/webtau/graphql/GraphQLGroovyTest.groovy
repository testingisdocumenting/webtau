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

import org.junit.Test

import static GraphQL.graphql

class GraphQLGroovyTest extends GraphQLTestBase {
    @Test
    void "execute"() {
        def query = '''
query {
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
'''

        def expectedIds = ["a", "b", "c"]

        def ids = graphql.execute(query) {
            errors.should == null

            // Make sure the full path as well as all shortcuts work
            body.data.allTasks.id.should == expectedIds
            data.allTasks.id.should == expectedIds
            allTasks.id.should == expectedIds
            id.should == expectedIds

            return allTasks.id
        }

        ids.should == expectedIds
    }

    @Test
    void "execute multiple queries"() {
        def query = '''
query {
    allTasks(uncompletedOnly: false) {
        id
    }
    taskById(id: "a") {
        id
    }
}
'''
        graphql.execute(query) {
            errors.should == null
            allTasks.id.should == ["a", "b", "c"]
            taskById.id.should == "a"

            // The shortcut to avoid specifying the query name in the path should not apply to multi-query requests
            id.should == null
        }
    }

    @Test
    void "execute with variables"() {
        String query = '''
query taskById($id: ID!) {
    taskById(id: $id) {
        id
        description
        completed
    }
}
'''

        def id = "a";
        def variables = [id: id]

        graphql.execute(query, variables) {
            errors.should == null
            taskById.id.should == id
        }
    }
}
