/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TestListeners {
    private static final List<TestListener> discoveredListeners = ServiceLoaderUtils.load(TestListener.class);
    private static final List<TestListener> addedListeners = new ArrayList<>();

    private static final ThreadLocal<Boolean> disabled = ThreadLocal.withInitial(() -> false);

    private TestListeners() {
    }

    public static <R> R withDisabledListeners(Supplier<R> code) {
        try {
            disabled.set(true);
            return code.get();
        } finally {
            disabled.set(false);
        }
    }

    public static void beforeTestRun(WebTauTest test) {
        listenersToUse().forEach(listener -> listener.beforeTestRun(test));
    }

    public static void afterTestRun(WebTauTest test) {
        listenersToUse().forEach(listener -> listener.afterTestRun(test));
    }

    public static void beforeFirstTest() {
        listenersToUse().forEach(TestListener::beforeFirstTest);
    }

    public static void afterAllTests() {
        listenersToUse().forEach(TestListener::afterAllTests);
    }

    public static void beforeFirstTestStatement(WebTauTest test) {
        listenersToUse().forEach(listeners -> listeners.beforeFirstTestStatement(test));
    }

    public static void afterLastTestStatement(WebTauTest test) {
        listenersToUse().forEach(listeners -> listeners.afterLastTestStatement(test));
    }

    public static void add(TestListener listener) {
        addedListeners.add(listener);
    }

    public static void remove(TestListener listener) {
        addedListeners.remove(listener);
    }

    public static void clearAdded() {
        addedListeners.clear();
    }

    private static Stream<TestListener> listenersToUse() {
        return disabled.get() ?
                Stream.empty():
                Stream.concat(discoveredListeners.stream(), addedListeners.stream());
    }
}
