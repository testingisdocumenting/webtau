package com.twosigma.webtau.data.table;

public interface TableDataCellFunction<T, R> {
    R apply(int rowIdx, int colIdx, String columnName, T v);
}
