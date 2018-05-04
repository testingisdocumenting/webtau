package com.twosigma.webtau.expectation
class ExpectationExtension {
    static void should(actual, ValueMatcher valueMatcher) {
        new ActualValue(actual).should(valueMatcher)
    }

    static void shouldNot(actual, ValueMatcher valueMatcher) {
        new ActualValue(actual).shouldNot(valueMatcher)
    }

    static Should getShould(actual) {
        return new Should(actual)
    }

    static ShouldNot getShouldNot(actual) {
        return new ShouldNot(actual)
    }
}
