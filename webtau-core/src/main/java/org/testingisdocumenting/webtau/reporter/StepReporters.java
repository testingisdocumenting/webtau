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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StepReporters {
    private static final StepReporter defaultStepReporter =
            new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter());

    private static final List<StepReporter> reporters = Collections.synchronizedList(
            ServiceLoaderUtils.load(StepReporter.class));

    private static final ThreadLocal<List<StepReporter>> localReporters = ThreadLocal.withInitial(ArrayList::new);

    public static void add(StepReporter reporter) {
        reporters.add(reporter);
    }

    public static void remove(StepReporter reporter) {
        reporters.remove(reporter);
    }

    public static <R> R withAdditionalReporter(StepReporter reporter, Supplier<R> code) {
        try {
            addLocal(reporter);
            return code.get();
        } finally {
            removeLocal(reporter);
        }
    }

    public static void onStart(TestStep step) {
        getReportersStream().forEach(r -> r.onStepStart(step));
    }

    public static void onSuccess(TestStep step) {
        getReportersStream().forEach(r -> r.onStepSuccess(step));
    }

    public static void onFailure(TestStep step) {
        getReportersStream().forEach(r -> r.onStepFailure(step));
    }

    private static Stream<StepReporter> getReportersStream() {
        List<StepReporter> localReporters = StepReporters.localReporters.get();

        if (reporters.isEmpty() && localReporters.isEmpty()) {
            return Stream.of(defaultStepReporter);
        }

        return Stream.concat(localReporters.stream(), reporters.stream());
    }

    private static void addLocal(StepReporter handler) {
        localReporters.get().add(handler);
    }

    private static void removeLocal(StepReporter handler) {
        localReporters.get().remove(handler);
    }
}
