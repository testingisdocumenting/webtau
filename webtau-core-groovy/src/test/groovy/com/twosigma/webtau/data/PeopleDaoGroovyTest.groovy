package com.twosigma.webtau.data

import com.twosigma.webtau.data.table.TableData
import org.junit.Test

class PeopleDaoGroovyTest {
    private PeopleDao dao = new PeopleDao()

    @Test
    void "provides access to new joiners"() {
        TableData allEmployees = ["id"    | "level" | "monthsAtCompany"] {
                                  ______________________________________
                                  "alice" |       5 |  1
                                  "bob"   |       3 |  0
                                  "smith" |       4 |  1
                                  "cat"   |       4 |  0 }

        addEmployees(allEmployees)

        dao.thisWeekJoiners().should == ["id"    | "level" | "monthsAtCompany"] {
                                         ______________________________________
                                         "bob"   | 3       | 0
                                         "cat"   | 4       | 0  }
    }

    private void addEmployees(TableData allEmployees) {
        dao.add(allEmployees.rowsStream().collect {
            new Person(it.id, it.level, it.monthsAtCompany)
        })
    }
}
