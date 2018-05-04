package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.ExpectationHandler.Flow;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;

import java.util.function.Function;

import static com.twosigma.webtau.Ddjt.createActualPath;

public class ActualValue implements ActualValueExpectations {
    private Object actual;

    public ActualValue(Object actual) {
        this.actual = actual;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ActualPath actualPath = extractPath(actual);
        boolean matches = valueMatcher.matches(actualPath, actual);

        if (!matches) {
            handleMismatch(valueMatcher, actualPath);
        }
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ActualPath actualPath = extractPath(actual);
        boolean matches = valueMatcher.negativeMatches(actualPath, actual);

         // TODO handlers
        if (!matches) {
            throw new AssertionError(valueMatcher.negativeMismatchedMessage(actualPath, actual));
        }
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> result);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> ! result);
    }

    private void waitImpl(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis,
                         Function<Boolean, Boolean> terminate) {
        ActualPath actualPath = extractPath(actual);

        expectationTimer.start();
        while (! expectationTimer.hasTimedOut(timeOutMillis)) {
            boolean matches = valueMatcher.matches(actualPath, actual);
            if (terminate.apply(matches)) {
                return;
            }

            expectationTimer.tick(tickMillis);
        }

        handleMismatch(valueMatcher, actualPath);
    }

    private void handleMismatch(ValueMatcher valueMatcher, ActualPath actualPath) {
        final String message = valueMatcher.mismatchedMessage(actualPath, actualPath);
        final Flow flow = ExpectationHandlers.onValueMismatch(actualPath, actual, message);

        if (flow != Flow.Terminate) {
            throw new AssertionError("\n" + message);
        }
    }

    private ActualPath extractPath(Object actual) {
        return (actual instanceof ActualPathAware) ?
            (((ActualPathAware) actual).actualPath()):
            createActualPath("[value]");
    }
}
