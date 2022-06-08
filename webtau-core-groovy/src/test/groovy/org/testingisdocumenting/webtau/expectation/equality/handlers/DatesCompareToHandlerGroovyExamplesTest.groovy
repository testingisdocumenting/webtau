/*
 * Copyright 2022 webtau maintainers
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

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal
import static org.testingisdocumenting.webtau.Matchers.greaterThan

class DatesCompareToHandlerGroovyExamplesTest {
    @Test
    void "actual local date string greater than expected local date instance"() {
        def dateAsText = "2018-06-10"
        dateAsText.shouldBe > LocalDate.of(2018, 6, 9)
    }

    @Test
    void "actual zoned date time string greater than expected local date instance"() {
        def timeAsText = "2018-01-02T00:00:00Z"
        timeAsText.shouldBe > LocalDate.of(2018, 1, 1) // date/time will be converted to local timezone
    }

    @Test
    void "should compare local date against local date time"() {
        def withTime = LocalDateTime.of(2022, 3, 16, 10, 4, 4)
        def dateOnly = LocalDate.of(2022, 3, 16)
        withTime.should == dateOnly // comparison ignores time portion when not provided
    }
}
