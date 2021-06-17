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

package org.testingisdocumenting.webtau.db

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class DbNamedParamsQueryTest {
    @Test
    void "convert named params to question marks outside quotes"() {
        def query = new DbNamedParamsQuery(
                "select * from table where count>:count and date<:date and last_count<:count",
                [count: 10, date: 'date'])

        query.questionMarksQuery.should == "select * from table where count>? and date<? and last_count<?"
        query.questionMarksValues.should == [10, 'date', 10]
    }

    @Test
    void "should ignore named params inside quotes"() {
        def query = new DbNamedParamsQuery(
                "select * from table where count>':c\"ount' and date<:date and last_count<\":count\"",
                [date: 'date'])

        query.questionMarksQuery.should == "select * from table where count>':c\"ount' and date<? and last_count<\":count\""
        query.questionMarksValues.should == ['date']
    }

    @Test
    void "handles parameter names with numeric prefix"() {
        def query = new DbNamedParamsQuery(
                "select * from table where id=:id1 or id=:id2", [id1: 1, id2: 2])

        query.questionMarksQuery.should == "select * from table where id=? or id=?"
        query.questionMarksValues.should == [1, 2]
    }

    @Test
    void "validates missing params"() {
        code {
            new DbNamedParamsQuery(
                    "select * from table where count>:count and date<:date and last_count<:count",
                    [count: 10])
        } should throwException("No parameter value found: date")
    }

    @Test
    void "understands single parameter value when there is only one unique placeholder"() {
        def query = new DbNamedParamsQuery(
                "select * from table where id=:id or another_id=:id", 25)

        query.questionMarksQuery.should == "select * from table where id=? or another_id=?"
        query.questionMarksValues.should == [25, 25]
    }

    @Test
    void "validates mismatch between single parameter and named placeholders"() {
        code {
            new DbNamedParamsQuery(
                    "select * from table where id=:id1 or another_id=:id2", 25)
        } should throwException("one no-name parameter value was provided, but have seen multiple placeholders: [id1, id2]")
    }
}
