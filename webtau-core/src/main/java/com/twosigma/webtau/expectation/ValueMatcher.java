package com.twosigma.webtau.expectation;

public interface ValueMatcher {
    // should

    /**
     * @return about to start matching message
     */
    String matchingMessage();

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return match message
     * @see ActualPath
     */
    String matchedMessage(ActualPath actualPath, Object actual);

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return mismatch message
     * @see ActualPath
     */
    String mismatchedMessage(ActualPath actualPath, Object actual);

    /**
     * Evaluates matcher. Called only for should
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a match
     * @see ActualPath
     */
    boolean matches(ActualPath actualPath, Object actual);

    // shouldNot

    /**
     * @return about to start negative matching (shouldNot case) message
     */
    String negativeMatchingMessage();

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative match message (shouldNot case)
     * @see ActualPath
     */
    String negativeMatchedMessage(ActualPath actualPath, Object actual);

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative mismatch message (shouldNot case)
     * @see ActualPath
     */
    String negativeMismatchedMessage(ActualPath actualPath, Object actual);

    /**
     * Evaluates matcher. Called only for shouldNot
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a negative match (shouldNot case)
     * @see ActualPath
     */
    boolean negativeMatches(ActualPath actualPath, Object actual);
}
