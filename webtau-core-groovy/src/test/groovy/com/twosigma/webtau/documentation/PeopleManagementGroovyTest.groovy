/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.documentation

import com.twosigma.webtau.data.table.TableData
import org.junit.Test

class PeopleManagementGroovyTest {
    private PeopleManagement peopleManagement = new PeopleManagement()

    @Test
    void "diversified teams should have various levels and time at company"() {
        def employeeData = [ "id"    | "level" | "monthsAtCompany"] {
                           _______________________________________
                             "bob"   |       2 |  12
                             "smith" |       4 |  34
                             "john"  |       3 |  20 }

        def diversified = peopleManagement.diversityLevel(employees(employeeData))
        diversified.should == true
    }

    private static List<Person> employees(TableData data) {
        return data.collect { r -> new Person(r.id, r.level, r.monthsAtCompany) }
    }
}
