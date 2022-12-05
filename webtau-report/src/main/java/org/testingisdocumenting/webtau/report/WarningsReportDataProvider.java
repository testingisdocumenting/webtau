/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.report;

import org.testingisdocumenting.webtau.reporter.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class WarningsReportDataProvider implements ReportDataProvider {
    public static final String ID = "warnings";

    @Override
    public Stream<WebTauReportCustomData> provide(WebTauTestList tests, WebTauReportLog log) {
        List<Map<String, Object>> warnings = new ArrayList<>();
        tests.forEach(test -> {
            Stream<WebTauStep> steps = test.stepsWithClassifier(WebTauStepClassifiers.WARNING);
            steps.map(step -> warningFromSingleStep(test, step)).forEach(warnings::add);
        });

        return Stream.of(new WebTauReportCustomData(ID, warnings));
    }

    private Map<String, Object> warningFromSingleStep(WebTauTest test, WebTauStep step) {
        Map<String, Object> result = new HashMap<>();
        result.put("testId", test.getId());
        result.put("message", step.getCompletionMessage().toString());
        result.put("input", step.getInput().toMap());

        return result;
    }
}
