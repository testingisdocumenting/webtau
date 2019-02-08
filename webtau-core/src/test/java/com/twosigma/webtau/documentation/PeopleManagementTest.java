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

package com.twosigma.webtau.documentation;

import com.twosigma.webtau.data.table.TableData;
import org.junit.Test;

import java.util.List;

import static com.twosigma.webtau.Ddjt.actual;
import static com.twosigma.webtau.Ddjt.equal;
import static com.twosigma.webtau.Ddjt.table;
import static java.util.stream.Collectors.toList;

public class PeopleManagementTest {
    private PeopleManagement peopleManagement = new PeopleManagement();

    @Test
    public void diversifiedTeamsShouldHaveVariousLevelsAndTimeAtCompany() {
        TableData employeeData = table(    "id", "level", "monthsAtCompany").values(
                                           "bob",       2, 12,
                                         "smith",       4, 34,
                                          "john",       3, 20);

        boolean diversified = peopleManagement.diversityLevel(employees(employeeData));
        actual(diversified).should(equal(true));
    }

    private List<Person> employees(TableData data) {
        return data.rowsStream().map(r -> new Person(r.get("id"), r.get("level"), r.get("monthsAtCompany")))
                .collect(toList());
    }
}
