package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open browser") {
    browser.open("/tables")
}

scenario("standard table equality") {
    // table-data-validation
    def summaryTable = browser.table("#summary")

    summaryTable.should == ["column A" | "column B"] {
                           ___________________________
                                  "A1" |       "B1"
                                  "A2" |       "B2" }
    // table-data-validation
}

scenario("simple ag grid equality") {
    def summaryTable = browser.table("#ag-grid-simple")

    summaryTable.should == ["column A" | "column B" | "column C"] {
                           ______________________________________
                                  "A1" |       "B1" | "C1"
                                  "A2" |       "B2" | "C2"
                                  "A3" |       "B3" | "C3" }
}
