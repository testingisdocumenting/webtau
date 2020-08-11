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
import org.testingisdocumenting.webtau.utils.ResourceUtils
import org.junit.Before

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.graphql.TestUtils.validationResult

class GraphQLCoverageTest {
    private GraphQLCoverage coverage

    @Before
    void setUp() {
        def schemaUrl = Paths.get(ResourceUtils.resourceUrl('test-schema.graphql').toURI()).toString()
        coverage = new GraphQLCoverage(new GraphQLSchema(schemaUrl))
    }

    @Test
    void "should provide non covered queries"() {
        coverage.recordQuery(validationResult('allTasks', GraphQLQueryType.QUERY))
        coverage.recordQuery(validationResult('complete', GraphQLQueryType.MUTATION))

        coverage.nonCoveredQueries().should == ['*name'     | '*type'    ] {
                                                   ____________________________________
                                                 'taskById' | GraphQLQueryType.QUERY
                                               'uncomplete' | GraphQLQueryType.MUTATION }
    }
}
