package org.testingisdocumenting.webtau.data.math;

public class NumbersSimpleMathHandler implements SimpleMathHandler {
    @Override
    public boolean handleAddSubtract(Object left, Object right) {
        return areBothNumbers(left, right);
    }

    @Override
    public Object add(Object left, Object right) {
        return addWithSign((Number) left, (Number) right, 1);
    }

    @Override
    public Object subtract(Object left, Object right) {
        return addWithSign((Number) left, (Number) right, -1);
    }

    @Override
    public boolean handleMultiplyDivide(Object left, Object right) {
        return areBothNumbers(left, right);
    }

    @Override
    public Object multiply(Object left, Object right) {
        return throwNotImplYet(left, right);
    }

    @Override
    public Object divide(Object left, Object right) {
        return throwNotImplYet(left, right);
    }

    private boolean areBothNumbers(Object left, Object right) {
        return left instanceof Number && right instanceof Number;
    }

    private Number addWithSign(Number left, Number right, Integer sign) {
        if (left instanceof Double || right instanceof Double) {
            return left.doubleValue() + sign * right.doubleValue();
        }

        if (left instanceof Long || right instanceof Long) {
            return left.longValue() + sign * right.longValue();
        }

        if (right instanceof Integer) {
            return left.intValue() + sign * right.intValue();
        }

        return throwNotImplYet(left, right);
    }

    private Number throwNotImplYet(Object left, Object right) {
        throw new UnsupportedOperationException("not implemented for <" + left.getClass() +
                "> and <" + right.getClass() + ">");
    }
}
