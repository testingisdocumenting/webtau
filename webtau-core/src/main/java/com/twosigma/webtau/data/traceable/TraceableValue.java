/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.data.traceable;

import java.util.function.Supplier;

public class TraceableValue {
    private static ThreadLocal<Boolean> isTracingDisabled = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private static ThreadLocal<TraceableValueModifier> traceableValueModifier = ThreadLocal.withInitial(() -> null);

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

        TraceableValueModifier modifier = TraceableValue.traceableValueModifier.get();
        newCheckLevel = modifier != null ? modifier.modify(newCheckLevel) : newCheckLevel;

        if (newCheckLevel.ordinal() > checkLevel.ordinal()) {
            checkLevel = newCheckLevel;
        }
    }

    public CheckLevel getCheckLevel() {
        return checkLevel;
    }

    public static <R> R withDisabledChecks(Supplier<R> code) {
        try {
            isTracingDisabled.set(true);
            return code.get();
        } finally {
            isTracingDisabled.set(false);
        }
    }
}
