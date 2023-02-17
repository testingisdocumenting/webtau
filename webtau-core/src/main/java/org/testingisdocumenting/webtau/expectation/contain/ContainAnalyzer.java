/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain;

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.contain.handlers.IterableContainHandler;
import org.testingisdocumenting.webtau.expectation.contain.handlers.NullContainHandler;
import org.testingisdocumenting.webtau.expectation.equality.ActualPathMessage;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ContainAnalyzer {
    private static final List<ContainHandler> handlers = discoverHandlers();

    private final List<ActualPathMessage> mismatches;

    public static ContainAnalyzer containAnalyzer() {
        return new ContainAnalyzer();
    }

    public boolean contains(ValuePath actualPath, Object actual, Object expected) {
        return contains(actual, expected,
                (handler) -> handler.analyzeContain(this, actualPath, actual, expected));
    }

    public boolean notContains(ValuePath actualPath, Object actual, Object expected) {
        return contains(actual, expected,
                (handler) -> handler.analyzeNotContain(this, actualPath, actual, expected));
    }

    public void reportMismatch(ContainHandler reporter, ValuePath actualPath, TokenizedMessage mismatch) {
        mismatches.add(new ActualPathMessage(actualPath, mismatch));
    }

    public TokenizedMessage generateMismatchReport() {
        List<TokenizedMessage> reports = new ArrayList<>();
        if (!mismatches.isEmpty()) {
            reports.add(TokenizedMessage.join("\n", mismatches.stream().map(ActualPathMessage::getFullMessage).collect(Collectors.toList())));
        }

        return TokenizedMessage.join("\n\n", reports);
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
        result.addAll(ServiceLoaderUtils.load(ContainHandler.class));
        result.add(new IterableContainHandler());

        return result;
    }

    private RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
                "no contains handler found for\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
                        "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }
}
