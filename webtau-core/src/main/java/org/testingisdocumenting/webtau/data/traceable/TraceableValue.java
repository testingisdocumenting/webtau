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

package org.testingisdocumenting.webtau.data.traceable;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.utils.TypeUtils;

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.data.render.PrettyPrinter.*;

public class TraceableValue implements PrettyPrintable {
    private static final Object[] PASS_STYLE = new Object[]{FontStyle.BOLD, Color.GREEN};
    private static final Object[] FAIL_STYLE = new Object[]{FontStyle.BOLD, Color.RED};

    private static final ThreadLocal<Boolean> isTracingDisabled = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private static final ThreadLocal<Boolean> isAlwaysFuzzyPassedTracing = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private CheckLevel checkLevel;
    private final Object value;

    public TraceableValue(Object value) {
        this.checkLevel = CheckLevel.None;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }

    public void updateCheckLevel(CheckLevel newCheckLevel) {
        if (isTracingDisabled.get()) {
            return;
        }

        CheckLevel checkLevelToUse = isAlwaysFuzzyPassedTracing.get() ? CheckLevel.FuzzyPassed : newCheckLevel;

        if (checkLevelToUse.ordinal() > checkLevel.ordinal()) {
            checkLevel = checkLevelToUse;
        }
    }

    public CheckLevel getCheckLevel() {
        return checkLevel;
    }

    /**
     * wrap code with this when values marking is not required
     * (e.g. using for just comparison)
     *
     * @param code code to execute
     * @param <R> type of the returned value
     * @return value returned by the passed code
     */
    public static <R> R withDisabledChecks(Supplier<R> code) {
        try {
            isTracingDisabled.set(true);
            return code.get();
        } finally {
            isTracingDisabled.set(false);
        }
    }

    /**
     * wrap code with this when values need to be marked as touched even though the actual comparison may have failed
     * (e.g. using nodes inside if-else like constructs)
     *
     * @param code code to execute
     * @param <R> type of the returned value
     * @return value returned by the passed code
     */
    public static <R> R withAlwaysFuzzyPassedChecks(Supplier<R> code) {
        try {
            isAlwaysFuzzyPassedTracing.set(true);
            return code.get();
        } finally {
            isAlwaysFuzzyPassedTracing.set(false);
        }
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        String surroundWith = printSurroundWith();

        printer.print(valuePrintStyle());
        printer.print(surroundWith);
        printer.print(convertToStringForPrint(getValue()));
        printer.print(surroundWith);
    }

    private Object convertToStringForPrint(Object value) {
        if (value == null) {
            return "null";
        }

        return TypeUtils.isString(value) ?
                "\"" + value + "\"" :
                value.toString();
    }

    private String printSurroundWith() {
        switch (getCheckLevel()) {
            case FuzzyFailed:
            case ExplicitFailed:
                return "**";
            case ExplicitPassed:
                return "__";
            case FuzzyPassed:
                return "~~";
            default:
                return "";
        }
    }
    private Object[] valuePrintStyle() {
        switch (getCheckLevel()) {
            case FuzzyFailed:
            case ExplicitFailed:
                return FAIL_STYLE;
            case FuzzyPassed:
            case ExplicitPassed:
                return PASS_STYLE;
            default:
                return new Color[]{value == null ?
                        UNKNOWN_COLOR :
                        TypeUtils.isString(value) ? STRING_COLOR : NUMBER_COLOR};
        }
    }
}
