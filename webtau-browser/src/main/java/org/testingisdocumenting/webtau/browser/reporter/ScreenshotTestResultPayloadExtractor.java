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

package org.testingisdocumenting.webtau.browser.reporter;

import org.testingisdocumenting.webtau.reporter.TestResultPayload;
import org.testingisdocumenting.webtau.reporter.TestResultPayloadExtractor;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Optional;
import java.util.stream.Stream;

public class ScreenshotTestResultPayloadExtractor implements TestResultPayloadExtractor {
    @Override
    public Stream<TestResultPayload> extract(Stream<WebTauStep> testSteps) {
        Stream<ScreenshotStepOutput> outputs = testSteps
                .flatMap(s -> s.collectOutputsOfType(ScreenshotStepOutput.class));

        Optional<ScreenshotStepOutput> first = outputs.findFirst();
        return first.map(screenshotStepOutput -> Stream.of(
                new TestResultPayload("screenshot", screenshotStepOutput.getBase64png())))
                .orElseGet(Stream::empty);
    }
}
