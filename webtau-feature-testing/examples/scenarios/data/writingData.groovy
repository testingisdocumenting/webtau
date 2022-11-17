package scenarios.data

import org.testingisdocumenting.webtau.data.table.TableData

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("csv list of maps") {
    // csv-list-data
    def list = [
        ["colA": 1, "colB": "R1"],
        ["colA": 2, "colB": "R2"]]
    // csv-list-data

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
    // csv-table-data
    TableData table = ["id"  | "value"] {
                       ________________
                       "id1" | "value1"
                       "id2" | "value2" }
    // csv-table-data

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

scenario("json list of maps") {
    // json-list-data
    def list = [
            ["colA": 1, "colB": "R1"],
            ["colA": 2, "colB": "R2"]]
    // json-list-data

    def resultPath = doc.console.capture("data-json-write-list") {
        // write-json-list-maps
        def path = data.json.write("generated/from-list-maps.json", list)
        // write-json-list-maps
        return path
    }

    resultPath.should == cfg.workingDir.resolve("generated/from-list-maps.json").toAbsolutePath()
    fs.textContent(resultPath).should == "[ {\n" +
            "  \"colA\" : 1,\n" +
            "  \"colB\" : \"R1\"\n" +
            "}, {\n" +
            "  \"colA\" : 2,\n" +
            "  \"colB\" : \"R2\"\n" +
            "} ]"
}

scenario("json map") {
    // json-map-data
    def map = ["colA": 1, "colB": "R1"]
    // json-map-data

    def resultPath = doc.console.capture("data-json-write-map") {
        // write-json-map
        def path = data.json.write("generated/from-map.json", map)
        // write-json-map
        return path
    }

    resultPath.should == cfg.workingDir.resolve("generated/from-map.json").toAbsolutePath()
    fs.textContent(resultPath).should == "{\n" +
            "  \"colA\" : 1,\n" +
            "  \"colB\" : \"R1\"\n" +
            "}"
}

scenario("json table data") {
    // json-table-data
    TableData table = ["id"  | "value"] {
                       ________________
                       "id1" | "value1"
                       "id2" | "value2" }
    // json-table-data

    def resultPath = doc.console.capture("data-json-write-table") {
        // write-json-table
        def path = data.json.write("generated/from-table-data.json", table)
        // write-json-table
        return path
    }

    resultPath.should == cfg.workingDir.resolve("generated/from-table-data.json").toAbsolutePath()
    fs.textContent(resultPath).should == "[ {\n" +
            "  \"id\" : \"id1\",\n" +
            "  \"value\" : \"value1\"\n" +
            "}, {\n" +
            "  \"id\" : \"id2\",\n" +
            "  \"value\" : \"value2\"\n" +
            "} ]"
}
