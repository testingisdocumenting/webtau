package com.twosigma.webtau.data.traceable;

public class TraceableValue {
    private CheckLevel checkLevel;
    private final Object value;

    public TraceableValue(Object value) {
        this.checkLevel = CheckLevel.None;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }

    public void updateCheckLevel(CheckLevel newCheckLevel) {
        if (newCheckLevel.ordinal() > checkLevel.ordinal()) {
            checkLevel = newCheckLevel;
        }
    }

    public CheckLevel getCheckLevel() {
        return checkLevel;
    }
}
