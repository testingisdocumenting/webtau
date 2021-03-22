/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http.validation;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HttpValidationHandlers {
    private static final List<HttpValidationHandler> globalHandlers = ServiceLoaderUtils.load(HttpValidationHandler.class);
    private static final List<HttpValidationHandler> addedHandlers = new ArrayList<>();

    private static final ThreadLocal<List<HttpValidationHandler>> localHandlers = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<Boolean> enabled = ThreadLocal.withInitial(() -> true);

    public static void add(HttpValidationHandler handler) {
        addedHandlers.add(handler);
    }

    public static void remove(HttpValidationHandler handler) {
        addedHandlers.remove(handler);
    }

    public static void clearAdded() {
        addedHandlers.clear();
    }

    public static <R> R withDisabledHandlers(Supplier<R> code) {
        enabled.set(false);
        try {
            return code.get();
        } finally {
            enabled.set(true);
        }
    }

    public static <R> R withAdditionalHandler(HttpValidationHandler handler, Supplier<R> code) {
        try {
            addLocal(handler);
            return code.get();
        } finally {
            removeLocal(handler);
        }
    }

    public static void validate(HttpValidationResult validationResult) {
        if (enabled.get()) {
            Stream.concat(addedHandlers.stream(),
                    Stream.concat(localHandlers.get().stream(), globalHandlers.stream()))
                            .forEach(c -> c.validate(validationResult));
        }
    }

    private static void addLocal(HttpValidationHandler handler) {
        localHandlers.get().add(handler);
    }

    private static void removeLocal(HttpValidationHandler handler) {
        localHandlers.get().remove(handler);
    }
}
