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

package com.twosigma.webtau.expectation.contain;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.handlers.NullContainHandler;
import com.twosigma.webtau.expectation.equality.ActualPathMessage;
import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;

public class ContainAnalyzer {
    private static List<ContainHandler> handlers = discoverHandlers();

    private final List<ActualPathMessage> mismatches;

    public static ContainAnalyzer containAnalyzer() {
        return new ContainAnalyzer();
    }

    public boolean contains(ActualPath actualPath, Object actual, Object expected) {
        return contains(actual, expected,
                (handler) -> handler.analyzeContain(this, actualPath, actual, expected));
    }

    public boolean containsNot(ActualPath actualPath, Object actual, Object expected) {
        return contains(actual, expected,
                (handler) -> handler.analyzeNotContain(this, actualPath, actual, expected));
    }

    public void reportMismatch(ContainHandler reporter, ActualPath actualPath, String mismatch) {
        mismatches.add(new ActualPathMessage(actualPath, mismatch));
    }

    public String generateMismatchReport() {
        List<String> reports = new ArrayList<>();
        if (!mismatches.isEmpty()) {
            reports.add(mismatches.stream().map(ActualPathMessage::getFullMessage).collect(joining("\n")));
        }

        return reports.stream().collect(joining("\n\n"));
    }

    public boolean hasMismatches() {
        return mismatches.isEmpty();
    }

    private ContainAnalyzer() {
        this.mismatches = new ArrayList<>();
    }

    private boolean contains(Object actual, Object expected, Consumer<ContainHandler> handle) {
        ContainHandler handler = handlers.stream().
                filter(h -> h.handle(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));

        int before = mismatches.size();
        handle.accept(handler);
        int after = mismatches.size();

        return after == before;
    }

    private static List<ContainHandler> discoverHandlers() {
        List<ContainHandler> result = new ArrayList<>();
        result.add(new NullContainHandler());
        result.addAll(ServiceUtils.discover(ContainHandler.class));

        return result;
    }

    private RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
                "no contains handler found for\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
                        "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }
}
