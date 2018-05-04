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
