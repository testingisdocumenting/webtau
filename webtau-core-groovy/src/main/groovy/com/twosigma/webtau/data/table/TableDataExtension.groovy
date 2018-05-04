package com.twosigma.webtau.data.table

class TableDataExtension {
    private static ThreadLocal<List<RowValues>> rows = new ThreadLocal<>()

    static List or(String columnNameA, String columnNameB) {
        return [columnNameA, columnNameB]
    }

    static List or(List header, String columnName) {
        header.add(columnName)
        return header
    }

    static TableData call(List header, Closure tableDataCode) {
        try {
            def tableData = new TableData(header.flatten().stream())

            rows.set(new ArrayList<>())

            use(TableBuildCategory) {
                Closure tableDataCodeToRun = tableDataCode.clone() as Closure
                tableDataCodeToRun.delegate = new TableBuildDelegate()
                tableDataCodeToRun.resolveStrategy = Closure.DELEGATE_FIRST

                tableDataCodeToRun.call()
            }

            rows.get().each { tableData.addRow(it.values.stream()) }
            return tableData
        } finally {
            rows.remove()
        }
    }

    static class RowValues {
        private List<Object> values = []
        RowValues(a, b) {
            values.add(a)
            values.add(b)
        }

        def or(Object o) {
            values.add(o)
            return this
        }
    }

    static class TableBuildDelegate {
        def getProperty(String name) {
            if (name =~ /^__+$/)
                return name

            return super.getProperty(name)
        }
    }

    static class TableBuildCategory {
        static or(Number a, Number b) {
            return addRow(new RowValues(a, b))
        }

        static or(String a, String b) {
            return addRow(new RowValues(a, b))
        }

        static or(Object a, Object b) {
            return addRow(new RowValues(a, b))
        }

        private static RowValues addRow(RowValues rowValues) {
            TableDataExtension.rows.get().add(rowValues)
            return rowValues
        }
    }
}
