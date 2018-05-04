package com.twosigma.webtau.expectation.contain;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.handlers.NullContainHandler;
import com.twosigma.webtau.expectation.equality.Mismatch;
import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class ContainAnalyzer {
    private static List<ContainHandler> handlers = discoverHandlers();

    private final boolean isNegative;
    private final List<Mismatch> mismatches;

    public static ContainAnalyzer containAnalyzer() {
        return new ContainAnalyzer(false);
    }

    public static ContainAnalyzer negativeContainAnalyzer() {
        return new ContainAnalyzer(true);
    }

    public boolean contains(ActualPath actualPath, Object actual, Object expected) {
        ContainHandler handler = handlers.stream().
                filter(h -> h.handle(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));

        int before = mismatches.size();
        handler.analyze(this, actualPath, actual, expected);
        int after = mismatches.size();

        return after == before;
    }

    public void reportMismatch(ContainHandler reporter, ActualPath actualPath, String mismatch) {
        mismatches.add(new Mismatch(actualPath, mismatch));
    }

    public String generateMismatchReport() {
        List<String> reports = new ArrayList<>();
        if (!mismatches.isEmpty()) {
            reports.add("does not contain:\n\n" +
                    mismatches.stream().map(Mismatch::fullMessage).collect(joining("\n")));
        }

        return reports.stream().collect(joining("\n\n"));
    }

    public boolean contains() {
        boolean isMismatch = mismatches.isEmpty();
        return isNegative() != isMismatch;
    }

    /**
     * comparator operates in two mode: should and shouldNot
     * @return true if the mode is shouldNot
     */
    public boolean isNegative() {
        return isNegative;
    }

    private ContainAnalyzer(boolean isNegative) {
        this.isNegative = isNegative;
        this.mismatches = new ArrayList<>();
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
