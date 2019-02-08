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

package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.ExpectationHandler.Flow;
import com.twosigma.webtau.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExpectationHandlers {
    private static List<ExpectationHandler> globalHandlers = ServiceUtils.discover(ExpectationHandler.class);
    private static ThreadLocal<List<ExpectationHandler>> localHandlers = ThreadLocal.withInitial(ArrayList::new);

    public static void add(ExpectationHandler handler) {
        globalHandlers.add(handler);
    }

    public static void remove(ExpectationHandler handler) {
        globalHandlers.remove(handler);
    }

    public static <R> R withAdditionalHandler(ExpectationHandler handler, Supplier<R> code) {
        try {
            addLocal(handler);
            return code.get();
        } finally {
            removeLocal(handler);
        }
    }

    public static Flow onValueMismatch(ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue, String message) {
        return Stream.concat(localHandlers.get().stream(), globalHandlers.stream())
                .map(h -> h.onValueMismatch(valueMatcher, actualPath, actualValue, message))
                .filter(flow -> flow == Flow.Terminate)
                .findFirst().orElse(Flow.PassToNext);
    }

    private static void addLocal(ExpectationHandler handler) {
        localHandlers.get().add(handler);
    }

    private static void removeLocal(ExpectationHandler handler) {
        localHandlers.get().remove(handler);
    }
}
