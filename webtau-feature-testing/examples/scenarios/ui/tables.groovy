package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open browser") {
    browser.open("/tables")
}

scenario("standard table equality") {
    // table-data-validation
    def summaryTable = browser.table("#summary")

    summaryTable.should == [ "column A" | "column B" | "column C"] {
                           _______________________________________
                                  "A-1" |      "B-1" | "C-1"
                                  "A-2" |      "B-2" | "C-2" }
    // table-data-validation
}

scenario("extract single table") {
    // extract-single-table-data
    def summaryTable = browser.table("#summary")
    def tableData = summaryTable.extractTableData()

    data.csv.write("table-data.csv", tableData)
    // extract-single-table-data

    defer {
        fs.delete("table-data.csv")
    }
}

scenario("extract from each table") {
    // extract-all-table-data
    def tablesList = browser.table("table")
    def combinedTableData = tablesList.extractAndMergeTableData()

    data.csv.write("combined-table-data.csv", combinedTableData)
    // extract-all-table-data

    defer {
        fs.delete("combined-table-data.csv")
    }

    combinedTableData.should == [ "column A" | "column B" | "column C"] {
                                 _______________________________________
                                       "A-1" |      "B-1" | "C-1"
                                       "A-2" |      "B-2" | "C-2"
                                       "A@1" |      "B@1" | "C@1"
                                       "A@2" |      "B@2" | "C@2" }
}

scenario("simple ag grid equality") {
    def summaryTable = browser.table("#ag-grid-simple")

    summaryTable.should == ["column A" | "column B" | "column C"] {
                           ______________________________________
                                  "A1" |       "B1" | "C1"
                                  "A2" |       "B2" | "C2"
                                  "A3" |       "B3" | "C3" }
}
