package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open browser") {
    browser.open("/tables")
}

scenario("table equality") {
    // table-data-validation
    def summaryTable = browser.table("#summary")

    summaryTable.should == ["column A" | "column B"] {
                           ___________________________
                                  "A1" |       "B1"
                                  "A2" |       "B2" }
    // table-data-validation
}

