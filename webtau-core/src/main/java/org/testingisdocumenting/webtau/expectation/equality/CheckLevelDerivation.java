package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;

public class CheckLevelDerivation {
    public static CheckLevel determineCompareToCheckLevel(CompareToResult result, CompareToComparator.AssertionMode assertionMode) {
        if (result.isGreater() && assertionMode == CompareToComparator.AssertionMode.GREATER_THAN ||
                result.isGreaterOrEqual() && assertionMode == CompareToComparator.AssertionMode.GREATER_THAN_OR_EQUAL ||
                result.isLess() && assertionMode == CompareToComparator.AssertionMode.LESS_THAN ||
                result.isLessOrEqual() && assertionMode == CompareToComparator.AssertionMode.LESS_THAN_OR_EQUAL) {
            return CheckLevel.FuzzyPassed;
        }

        if (result.isGreaterOrEqual() && assertionMode == CompareToComparator.AssertionMode.LESS_THAN ||
                result.isGreater() && assertionMode == CompareToComparator.AssertionMode.LESS_THAN_OR_EQUAL ||
                result.isLessOrEqual() && assertionMode == CompareToComparator.AssertionMode.GREATER_THAN ||
                result.isLess() && assertionMode == CompareToComparator.AssertionMode.GREATER_THAN_OR_EQUAL) {
            return CheckLevel.ExplicitFailed;
        }

        return CheckLevel.None;
    }

    public static CheckLevel determineEqualOnlyCheckLevel(CompareToResult result, CompareToComparator.AssertionMode assertionMode) {
        if (result.isNotEqual() && assertionMode == CompareToComparator.AssertionMode.EQUAL ||
                result.isEqual() && assertionMode == CompareToComparator.AssertionMode.NOT_EQUAL) {
            return CheckLevel.ExplicitFailed;
        }

        if (result.isEqual() && assertionMode == CompareToComparator.AssertionMode.EQUAL) {
            return CheckLevel.ExplicitPassed;
        }

        if (result.isNotEqual() && assertionMode == CompareToComparator.AssertionMode.NOT_EQUAL) {
            return CheckLevel.FuzzyPassed;
        }

        return CheckLevel.None;
    }
}
