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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.utils.TypeUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.expected;
import static org.testingisdocumenting.webtau.utils.TraceUtils.renderValueAndType;

public class DateAndStringCompareToHandler implements CompareToHandler {
    private static final ZoneId UTC = ZoneId.of("UTC");

    private static final List<FormatParser> parsers = Arrays.asList(
            new FormatParser(DateTimeFormatter.ISO_DATE_TIME, ZonedDateTime::parse),
            new FormatParser(DateTimeFormatter.ISO_LOCAL_DATE, LocalDate::parse));

    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return handle(actual, expected);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return handle(actual, expected);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Comparator dateComparator = new Comparator(comparator, actualPath, actual, expected, true);
        dateComparator.compare();
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Comparator dateComparator = new Comparator(comparator, actualPath, actual, expected, false);
        dateComparator.compare();
    }

    private boolean handle(Object actual, Object expected) {
        return TypeUtils.isString(actual) && (
                expected instanceof LocalDate ||
                        expected instanceof ZonedDateTime);
    }

    private class Comparator {
        private final CompareToComparator compareToComparator;
        private final ActualPath actualPath;
        private final TemporalAccessor actual;
        private final Object expected;
        private final boolean isEqualOnly;

        Comparator(CompareToComparator compareToComparator, ActualPath actualPath,
                   Object actual, Object expected, boolean isEqualOnly) {
            this.compareToComparator = compareToComparator;
            this.actualPath = actualPath;
            this.actual = actualToTemporalAccessor(actual);
            this.expected = expected;
            this.isEqualOnly = isEqualOnly;
        }

        void compare() {
            if (actual instanceof LocalDate && expected instanceof LocalDate) {
                compareLocalDates((LocalDate) actual, (LocalDate) expected);
            } else if (actual instanceof ZonedDateTime && expected instanceof LocalDate) {
                compareZonedDateTimeAndLocalDate((ZonedDateTime) actual, (LocalDate) expected);
            } else if (actual instanceof ZonedDateTime && expected instanceof ZonedDateTime) {
                compareZonedDateTimes((ZonedDateTime) actual, (ZonedDateTime) expected);
            } else {
                throw new UnsupportedOperationException("combination is not supported:\n" +
                        renderActualExpected(actual, expected));
            }
        }

        private void compareZonedDateTimes(ZonedDateTime actual, ZonedDateTime expected) {
            ZonedDateTime normalizedActual = actual.withZoneSameInstant(UTC);
            ZonedDateTime normalizedExpected = expected.withZoneSameInstant(UTC);

            report(normalizedActual.compareTo(normalizedExpected), renderActualExpectedWithNormalized(actual, expected,
                    normalizedActual, normalizedExpected));
        }

        private void compareZonedDateTimeAndLocalDate(ZonedDateTime actual, LocalDate expected) {
            report(actual.toLocalDate().compareTo(expected), renderActualExpected(actual, expected));
        }

        private void compareLocalDates(LocalDate actual, LocalDate expected) {
            report(actual.compareTo(expected), renderActualExpected(actual, expected));
        }

        private void report(int compareTo, String message) {
            if (isEqualOnly) {
                compareToComparator.reportEqualOrNotEqual(DateAndStringCompareToHandler.this,
                        compareTo == 0, actualPath, message);
            } else {
                compareToComparator.reportCompareToValue(DateAndStringCompareToHandler.this,
                        compareTo, actualPath, message);
            }
        }

        private TemporalAccessor actualToTemporalAccessor(Object actual) {
            String actualAsText = actual.toString();

            for (FormatParser parser: parsers) {
                try {
                    return parser.convert(actualAsText);
                } catch (DateTimeParseException ignored) {
                }
            }

            throw new UnsupportedOperationException("cannot parse " + actualAsText + "\navailable formats:\n" +
                    parsers.stream().map(FormatParser::toString).collect(Collectors.joining("\n")));
        }

        private String renderActualExpected(Object actual, Object expected) {
            return "  actual: " + renderValueAndType(actual) + "\n" +
                    expected(compareToComparator.getAssertionMode(), renderValueAndType(expected));
        }

        private String renderActualExpectedWithNormalized(ZonedDateTime actual, ZonedDateTime expected,
                                                          ZonedDateTime normalizedActual, ZonedDateTime normalizedExpected) {
            return "  actual: " + renderValueAndType(actual) + "(UTC normalized: " + normalizedActual + ")\n" +
                    expected(compareToComparator.getAssertionMode(), renderValueAndType(expected) + "(UTC normalized: " + normalizedExpected + ")");
        }
    }

    private static class FormatParser {
        DateTimeFormatter formatter;
        BiFunction<CharSequence, DateTimeFormatter, Temporal> instanceCreator;

        FormatParser(DateTimeFormatter formatter, BiFunction<CharSequence, DateTimeFormatter, Temporal> instanceCreator) {
            this.formatter = formatter;
            this.instanceCreator = instanceCreator;
        }

        Temporal convert(CharSequence text) {
            return instanceCreator.apply(text, formatter);
        }

        public String toString() {
            return formatter.format(ZonedDateTime.now());
        }
    }
}
