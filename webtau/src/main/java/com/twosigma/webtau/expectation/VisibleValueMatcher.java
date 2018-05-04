package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.page.ElementValue;

public class VisibleValueMatcher implements ValueMatcher {
    @Override
    public String matchingMessage() {
        return "to be visible";
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "is visible";
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return "is hidden";
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        ElementValue elementValue = (ElementValue) actual;
        return elementValue.getParent().isVisible();
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be hidden";
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "is hidden";
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return "is visible";
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        return ! matches(actualPath, actual);
    }
}
