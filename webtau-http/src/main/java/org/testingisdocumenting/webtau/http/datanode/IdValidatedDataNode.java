package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.expectation.ValueMatcher;

public interface IdValidatedDataNode extends DataNode {
    @Override
    default void should(ValueMatcher valueMatcher) {
        DataNodeIdValidators.validate(id());
        DataNode.super.should(valueMatcher);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        DataNodeIdValidators.validate(id());
        DataNode.super.shouldNot(valueMatcher);
    }
}
