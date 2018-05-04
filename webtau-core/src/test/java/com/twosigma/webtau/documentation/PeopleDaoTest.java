package com.twosigma.webtau.documentation;

import com.twosigma.webtau.data.table.TableData;
import org.junit.Test;

import static com.twosigma.webtau.Ddjt.actual;
import static com.twosigma.webtau.Ddjt.equal;
import static com.twosigma.webtau.Ddjt.header;

public class PeopleDaoTest {
    private PeopleDao dao = new PeopleDao();

    @Test
    public void providesAccessToNewJoiners() {
        // ...

        TableData expected = header("id", "level", "monthsAtCompany").values(
                                    "bob",      3,   0,
                                    "smith",    4,   0);

        actual(dao.thisWeekJoiners()).should(equal(expected));
    }
}