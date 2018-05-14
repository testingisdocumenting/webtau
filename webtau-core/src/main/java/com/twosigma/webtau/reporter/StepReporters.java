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

package com.twosigma.webtau.reporter;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.Set;
import java.util.stream.Stream;

public class StepReporters {
    private static final StepReporter defaultStepReporter =
            new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter());

    private static Set<StepReporter> reporters = ServiceLoaderUtils.load(StepReporter.class);

    public static void add(StepReporter reporter) {
        reporters.add(reporter);
    }

    public static void remove(StepReporter reporter) {
        reporters.remove(reporter);
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
        if (reporters.isEmpty()) {
            return Stream.of(defaultStepReporter);
        }

        return reporters.stream();
    }
}
