package org.testingisdocumenting.webtau.data.table.header;

public class CompositeKeyDefaultUnderlyingValueExtractor implements CompositeKeyUnderlyingValueExtractor {
    @Override
    public boolean handles(Object value) {
        return true;
    }

    @Override
    public Object extract(Object value) {
        return value;
    }
}
