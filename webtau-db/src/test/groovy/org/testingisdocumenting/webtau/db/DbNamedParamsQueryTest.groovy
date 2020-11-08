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
}
