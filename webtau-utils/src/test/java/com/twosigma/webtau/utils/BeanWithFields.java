package com.twosigma.webtau.utils;

public class BeanWithFields {
    private long longBackingProp;
    private String stringBackingField;

    public BeanWithFields(long longBackingProp, String stringBackingField) {
        this.longBackingProp = longBackingProp;
        this.stringBackingField = stringBackingField;
    }

    public long getPropA() {
        return longBackingProp;
    }

    public String getPropB() {
        return stringBackingField;
    }
}
