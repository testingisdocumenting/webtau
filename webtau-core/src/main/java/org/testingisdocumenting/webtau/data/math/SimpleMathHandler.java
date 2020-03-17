package org.testingisdocumenting.webtau.data.math;

public interface SimpleMathHandler {
    boolean handleAddSubtract(Object left, Object right);
    Object add(Object left, Object right);
    Object subtract(Object left, Object right);

    boolean handleMultiplyDivide(Object left, Object right);
    Object multiply(Object left, Object right);
    Object divide(Object left, Object right);
}
