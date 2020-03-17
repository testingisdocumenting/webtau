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

package com.twosigma.webtau.reporter;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestResultPayloadExtractors {
    private static final List<TestResultPayloadExtractor> extractors = Collections.synchronizedList(
            ServiceLoaderUtils.load(TestResultPayloadExtractor.class));

    public static Stream<TestResultPayload> extract(Stream<TestStep<?, ?>> testSteps) {
        List<TestStep<?, ?>> steps = testSteps.collect(Collectors.toList());
        return extractors.stream().flatMap(e -> e.extract(steps.stream()));
    }
}
