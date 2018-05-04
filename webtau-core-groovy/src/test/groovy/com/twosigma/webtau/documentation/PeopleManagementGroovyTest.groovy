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
