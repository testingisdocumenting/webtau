package scenarios.data

import org.testingisdocumenting.webtau.data.table.TableData

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("csv list of maps") {
    // list-data
    def list = [
        ["colA": 1, "colB": "R1"],
        ["colA": 2, "colB": "R2"]
    ]
    // list-data

    def resultPath = doc.console.capture("data-csv-write-list") {
        // write-csv-list-maps
        def path = data.csv.write("generated/from-list-maps.csv", list)
        // write-csv-list-maps
        return path
    }

    resultPath.should == cfg.workingDir.resolve("generated/from-list-maps.csv").toAbsolutePath()
    fs.textContent(resultPath).should == "colA,colB\r\n" +
            "1,R1\r\n" +
            "2,R2\r\n"
}

scenario("csv table data") {
    // table-data
    TableData table = ["id"  | "value"] {
                       ________________
                       "id1" | "value1"
                       "id2" | "value2" }
    // table-data            

    def resultPath = doc.console.capture("data-csv-write-table") {
        // write-csv-table
        def path = data.csv.write("generated/from-table-data.csv", table)
        // write-csv-table
        return path
    }

    resultPath.should == cfg.workingDir.resolve("generated/from-table-data.csv").toAbsolutePath()
    fs.textContent(resultPath).should == "id,value\r\n" +
            "id1,value1\r\n" +
            "id2,value2\r\n"
}