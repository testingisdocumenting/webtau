package com.twosigma.webtau.data.table.header;

/**
 * TableData cell values can consist of high value wrappers.
 * To consistently match table keys, composite key needs to be built from the original underlying values
 */
public interface CompositeKeyUnderlyingValueExtractor {
    boolean handles(Object value);
    Object extract(Object value);
}
