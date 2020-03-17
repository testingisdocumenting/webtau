package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.data.table.TableData;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class PeopleDaoTest {
    private PeopleDao dao = new PeopleDao();

    @Test
    public void providesAccessToNewJoiners() {
        TableData allEmployees = table(    "id", "level", "monthsAtCompany",
                                        ____________________________________,
                                        "alice",       5,   1,
                                          "bob",       3,   0,
                                        "smith",       4,   1,
                                          "cat",       4,   0);
        addEmployees(allEmployees);

        actual(dao.thisWeekJoiners()).should(equal(table(   "*id", "level", "monthsAtCompany",
                                                            ____________________________________,
                                                            "bob",       3,   0,
                                                            "cat",       4,   0)));
    }

    private void addEmployees(TableData allEmployees) {
        dao.add(allEmployees.rowsStream()
                .map(row -> new Person(row.get("id"), row.get("level"), row.get("monthsAtCompany")))
                .collect(Collectors.toList()));
    }
}