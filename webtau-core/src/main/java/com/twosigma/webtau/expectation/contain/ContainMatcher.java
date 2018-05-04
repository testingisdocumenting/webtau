package com.twosigma.webtau.expectation.contain;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;

public class ContainMatcher implements ValueMatcher {
    private ContainAnalyzer containAnalyzer;
    private final Object expected;

    public ContainMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public String matchingMessage() {
        return "co contain " + DataRenderers.render(expected);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "contains " + DataRenderers.render(expected);
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return containAnalyzer.generateMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        return analyzeContain(actualPath, actual);
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not contain " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "does not contain " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " contains " + DataRenderers.render(expected) + "\nactual: " +
                DataRenderers.render(actual);
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.negativeContainAnalyzer();
        return analyzeContain(actualPath, actual);
    }

    private boolean analyzeContain(ActualPath actualPath, Object actual) {
        containAnalyzer.contains(actualPath, actual, expected);
        return containAnalyzer.contains();
    }
}
