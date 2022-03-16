/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.junit.Test

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

import static org.testingisdocumenting.webtau.WebTauCore.*

class DatesCompareToHandlerTest {
    @Test
    void "handles local date and zoned data time as expected and string as actual"() {
        def handler = new DatesCompareToHandler()
        assert handler.handleEquality("2018-06-10", LocalDate.of(1, 1, 1))
        assert handler.handleEquality("2018-06-10", ZonedDateTime.of(2018, 10, 3,
            1, 1, 1, 1, ZoneId.of("UTC")))
    }

    @Test
    void "actual local date string greater than expected local date instance"() {
        actual("2018-06-10").shouldBe(greaterThan(LocalDate.of(2018, 6, 9)))
    }

    @Test
    void "actual local date gstring greater than expected local date instance"() {
        def month = '06'
        actual("2018-${month}-10").shouldBe(greaterThan(LocalDate.of(2018, 6, 9)))
    }

    @Test
    void "actual local date string equals expected local date instance"() {
        actual("2018-06-10").should(equal(LocalDate.of(2018, 6, 10)))
    }

    @Test
    void "actual local date string doesn't equal expected local date instance"() {
        code {
            actual("2018-06-10").should(equal(LocalDate.of(2018, 6, 9)))
        } should throwException("\nmismatches:\n" +
            "\n" +
            "[value]:   actual: 2018-06-10 <java.time.LocalDate>\n" +
            "         expected: 2018-06-09 <java.time.LocalDate>")
    }

    @Test
    void "actual zoned date time string greater than expected local date instance"() {
        actual("2018-01-02T00:00:00Z").shouldBe(greaterThan(LocalDate.of(2018, 1, 1)))
    }

    @Test
    void "actual zoned date time string equals expected local date instance, when should be greater"() {
        code {
            actual("2018-01-01T00:00:00Z").shouldBe(greaterThan(LocalDate.of(2018, 1, 1)))
        } should throwException("\nmismatches:\n\n" +
            "[value]:   actual: 2018-01-01T00:00Z <java.time.ZonedDateTime>\n" +
            "         expected: greater than 2018-01-01 <java.time.LocalDate>")
    }

    @Test
    void "actual zoned date time string greater than expected zoned date time instance"() {
        actual("2018-01-02T10:00:00-01:00:00").shouldBe(greaterThan(
            ZonedDateTime.of(2018, 1, 2, 10, 0, 0, 0, ZoneId.of("UTC"))))
    }

    @Test
    void "actual zoned date time string equals expected zoned date time instance"() {
        actual("2018-01-02T10:00:00+01:00:00").should(equal(
            ZonedDateTime.of(2018, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC"))))
    }

    @Test
    void "actual zoned date time string less than expected zoned date time instance, when should be greater"() {
        code {
            actual("2018-01-02T10:00:00+01:00:00").shouldBe(greaterThan(
                ZonedDateTime.of(2018, 1, 2, 10, 0, 0, 0, ZoneId.of("UTC"))))
        } should throwException("\nmismatches:\n" +
            "\n" +
            "[value]:   actual: 2018-01-02T10:00+01:00 <java.time.ZonedDateTime>(UTC normalized: 2018-01-02T09:00Z[UTC])\n" +
            "         expected: greater than 2018-01-02T10:00Z[UTC] <java.time.ZonedDateTime>(UTC normalized: 2018-01-02T10:00Z[UTC])")
    }

    @Test
    void "actual zoned date time string equals expected instance"() {
        def expectedInstance = ZonedDateTime.of(2018, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        actual("2018-01-02T10:00:00+01:00:00").should(equal(expectedInstance))
    }

    @Test
    void "actual zoned date time string greater than expected instance"() {
        def expectedInstance = ZonedDateTime.of(2018, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        actual("2018-01-02T11:00:00+01:00:00").shouldBe(greaterThan(expectedInstance))
    }

    @Test
    void "actual zoned date time string less than expected instance"() {
        def expectedInstance = ZonedDateTime.of(2018, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        actual("2018-01-02T09:00:00+01:00:00").shouldBe(lessThan(expectedInstance))
    }

    @Test
    void "actual zoned date time string equals expected instance when should not"() {
        code {
            def expectedInstance = ZonedDateTime.of(2018, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC")).toInstant()
            actual("2018-01-02T10:00:00+02:00:00").should(equal(expectedInstance))
        } should throwException("\nmismatches:\n" +
                "\n" +
                "[value]:   actual: 2018-01-02T10:00+02:00 <java.time.ZonedDateTime>(UTC normalized: 2018-01-02T08:00:00Z)\n" +
                "         expected: 2018-01-02T09:00:00Z <java.time.Instant>(UTC normalized: 2018-01-02T09:00:00Z)")
    }

    @Test
    void "reports that given text cannot be parsed and lists currently supported formats"() {
        code {
            actual("xyz").should(equal(LocalDate.of(2018, 1, 1)))
        } should throwException(~/cannot parse xyz
available formats:
.+/)
    }

    @Test
    void "should compare local date against local date time"() {
        def withTime = LocalDateTime.of(2022, 3, 16, 10, 4, 4)
        def withDate = LocalDate.of(2022, 3, 16)

        actual(withTime).should(equal(withDate))
    }

    @Test
    void "should report mismatch during local date against local date time"() {
        def withTime = LocalDateTime.of(2022, 3, 16, 10, 4, 4)
        def withDate = LocalDate.of(2022, 4, 16)

        code {
            actual(withTime).should(equal(withDate))
        } should throwException("\nmismatches:\n" +
                "\n" +
                "[value]:   actual: 2022-03-16T10:04:04 <java.time.LocalDateTime>\n" +
                "         expected: 2022-04-16 <java.time.LocalDate>")
    }
}
