package com.twosigma.webtau.data.time;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.BiConsumer;

/**
 * Date representation that is used only for expectation and input preparation of tests.
 * It will have its own rules for equality associated with it. Mainly to support fuzzy matches
 */
public class TestTime {
    private static final ZoneId UTC = ZoneId.of("UTC");

    private Integer year;
    private Month month;
    private Integer day;

    private Integer hours;
    private Integer minutes;
    private Integer seconds;
    private Integer nanoOfSecond;

    private ZoneId timeZone;

    public TestTime(Integer year, Month month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.timeZone = UTC;
    }

    public TestTime(Integer year, Month month, Integer day, Integer hours, Integer minutes, Integer seconds) {
        this(year, month, day);
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public static TestTime today(Integer hours, Integer minutes) {
        ZonedDateTime now = LocalDateTime.now().atZone(UTC);
        return new TestTime(now.getYear(), now.getMonth(), now.getDayOfMonth(), hours, minutes, 0);
    }
    
    public Integer getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHours() {
        return hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public Integer getNanoOfSecond() {
        return nanoOfSecond;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public boolean hasDatePart() {
        return hasYear() || hasMonth() || hasDay();
    }

    public boolean hasYear() {
        return year != null;
    }

    public boolean hasMonth() {
        return month != null;
    }

    public boolean hasDay() {
        return day != null;
    }

    public boolean hasTimePart() {
        return hasHours() || hasMinutes() || hasSeconds() || hasNanos();
    }

    public boolean hasHours() {
        return hours != null;
    }

    public boolean hasMinutes() {
        return minutes != null;
    }

    public boolean hasSeconds() {
        return seconds != null;
    }

    public boolean hasNanos() {
        return nanoOfSecond != null;
    }

    public Long toMillisSinceEpoch() {
        // TODO seconds and nanos
        return LocalDateTime.of(year, month, day,
            hours != null ? hours : 0,
            minutes != null ? minutes : 0).atZone(timeZone).toInstant().toEpochMilli();
    }

    @Override
    public String toString() {
        StringBuilder render = new StringBuilder();
        BiConsumer<String, Object> append = (name, value) -> {
            if (value != null) {
                render.append(name).append(":").append(value).append(" ");
            }
        };

        append.accept("year", year);
        append.accept("month", month);
        append.accept("day", day);
        append.accept("hours", hours);
        append.accept("minutes", minutes);
        append.accept("seconds", seconds);
        append.accept("nanoOfSecond", nanoOfSecond);
        append.accept("timeZone", timeZone);

        return render.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestTime testTime = (TestTime) o;

        if (year != null ? !year.equals(testTime.year) : testTime.year != null) {
            return false;
        }
        if (month != testTime.month) {
            return false;
        }
        if (day != null ? !day.equals(testTime.day) : testTime.day != null) {
            return false;
        }
        if (hours != null ? !hours.equals(testTime.hours) : testTime.hours != null) {
            return false;
        }
        if (minutes != null ? !minutes.equals(testTime.minutes) : testTime.minutes != null) {
            return false;
        }
        if (seconds != null ? !seconds.equals(testTime.seconds) : testTime.seconds != null) {
            return false;
        }
        if (nanoOfSecond != null ? !nanoOfSecond.equals(testTime.nanoOfSecond) : testTime.nanoOfSecond != null) {
            return false;
        }
        return timeZone != null ? timeZone.equals(testTime.timeZone) : testTime.timeZone == null;

    }

    @Override
    public int hashCode() {
        int result = year != null ? year.hashCode() : 0;
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (hours != null ? hours.hashCode() : 0);
        result = 31 * result + (minutes != null ? minutes.hashCode() : 0);
        result = 31 * result + (seconds != null ? seconds.hashCode() : 0);
        result = 31 * result + (nanoOfSecond != null ? nanoOfSecond.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        return result;
    }
}
