package com.twosigma.webtau.documentation;

import com.twosigma.webtau.data.table.TableData;
import org.junit.Test;

import java.util.List;

import static com.twosigma.webtau.Ddjt.actual;
import static com.twosigma.webtau.Ddjt.equal;
import static com.twosigma.webtau.Ddjt.header;
import static java.util.stream.Collectors.toList;

public class PeopleManagementTest {
    private PeopleManagement peopleManagement = new PeopleManagement();

    @Test
    public void diversifiedTeamsShouldHaveVariousLevelsAndTimeAtCompany() {
        TableData employeeData = header(    "id", "level", "monthsAtCompany").values(
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
