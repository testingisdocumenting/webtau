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

package com.twosigma.webtau.data.time

import com.twosigma.webtau.data.time.listener.TimeRangeContextListeners

import java.time.Month
class TestTimeExtension {
    static def minus(Integer year, Month month) {
        return new YearMonthDatePart(year: year, month: month)
    }

    static def minus(YearMonthDatePart yearMonthDatePart, DayTimeDatePart dayTimeDatePart) {
        return new TestTime(yearMonthDatePart.year, yearMonthDatePart.month, dayTimeDatePart.day,
                dayTimeDatePart.hoursMinutes.hours, dayTimeDatePart.hoursMinutes.minutes, 0)
    }

    static def minus(YearMonthDatePart yearMonthDatePart, Integer day) {
        return new YearMonthDayDatePart(year: yearMonthDatePart.year, month: yearMonthDatePart.month, day: day)
    }

    static def minus(YearMonthDatePart yearMonthDatePart, DayTimeCodePart dayTimeCodePart) {
        TestTime testTime = new TestTime(yearMonthDatePart.year, yearMonthDatePart.month, dayTimeCodePart.day,
                dayTimeCodePart.hoursMinutes.hours, dayTimeCodePart.hoursMinutes.minutes, 0)

        TimeRangeContextListeners.beforeCall(testTime, testTime)
        dayTimeCodePart.code.call()
        TimeRangeContextListeners.afterCall(testTime, testTime)
    }

    static def minus(TestTime rangeA, int yearB) {
        return new TimeRangeYearPart(a: rangeA, yearB: yearB)
    }

    static def minus(TimeRangeYearPart range, Month monthB) {
        return new TimeRangeYearMonthPart(a: range.a, yearB: range.yearB, monthB: monthB)
    }

    static void minus(TimeRangeYearMonthPart range, DayTimeCodePart dayTimeB) {
        TestTime rangeB = new TestTime(range.yearB, range.monthB, dayTimeB.day,
                dayTimeB.hoursMinutes.hours, dayTimeB.hoursMinutes.minutes, 0)

        TimeRangeContextListeners.beforeCall(range.a, rangeB)
        dayTimeB.code.call()
        TimeRangeContextListeners.afterCall(range.a, rangeB)
    }

    static void minus(TimeRangeYearMonthPart range, DayCodePart dayB) {
        TestTime rangeB = new TestTime(range.yearB, range.monthB, dayB.day)

        TimeRangeContextListeners.beforeCall(range.a, rangeB)
        dayB.code.call()
        TimeRangeContextListeners.afterCall(range.a, rangeB)
    }

    static def minus(YearMonthDayDatePart yearMonthDayDatePart, int yearB) {
        TestTime a = new TestTime(yearMonthDayDatePart.year, yearMonthDayDatePart.month, yearMonthDayDatePart.day)
        return new TimeRangeYearPart(a: a, yearB: yearB)
    }

    static def call(Integer day, Map hoursMinutesMap) {
        return new DayTimeDatePart(day: day, hoursMinutes: TestTimeDSLUtils.hoursMinutesFromMap(hoursMinutesMap))
    }

    static def call(Integer day, Map hoursMinutesMap, Closure code) {
        def hoursMinutes = TestTimeDSLUtils.hoursMinutesFromMap(hoursMinutesMap)
        return new DayTimeCodePart(day: day, hoursMinutes: hoursMinutes, code: code)
    }

    static def call(Integer day, Closure code) {
        return new DayCodePart(day: day, code: code)
    }
}
