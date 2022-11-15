/*
 * Copyright 2022 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.data;

import org.junit.Test;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.utils.FileUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.data.Data.data;

public class DataCsvJavaTest {
    @Test
    public void readTable() {
        // read-table
        TableData table = data.csv.table("data/table.csv");
        // read-table

        // validate-table
        actual(table.row(0).get("B")).should(equal("2"));
        actual(table.row(0).get("B").getClass().getCanonicalName()).should(equal("java.lang.String"));
        // validate-table
    }

    @Test
    public void readTableAutoConverted() {
        // read-table-auto-converted
        TableData table = data.csv.tableAutoConverted("data/table.csv");
        actual(table.row(0).get("B")).should(equal(2));
        actual(table.row(0).get("B").getClass().getCanonicalName()).should(equal("java.lang.Long"));
        // read-table-auto-converted
    }

    @Test
    public void readListOfMaps() {
        // read-list-of-maps
        List<Map<String, String>> list = data.csv.listOfMaps("data/table.csv");
        actual(list.get(0).get("B")).should(equal("2"));
        actual(list.get(0).get("B").getClass().getCanonicalName()).should(equal("java.lang.String"));
        // read-list-of-maps
    }

    @Test
    public void readListOfMapsAutoConverted() {
        // read-list-of-maps-auto-converted
        List<Map<String, ?>> list = data.csv.listOfMapsAutoConverted("data/table.csv");
        actual(list.get(0).get("B")).should(equal(2));
        actual(list.get(0).get("B").getClass().getCanonicalName()).should(equal("java.lang.Long"));
        // read-list-of-maps-auto-converted
    }

    @Test
    public void readListOfMapsHeader() {
        // read-list-of-maps-header
        List<Map<String, ?>> list = data.csv.listOfMapsAutoConverted(Arrays.asList("C1", "C2", "C3"), "data/table-no-header.csv");
        actual(list.get(0).get("C2")).should(equal(2));
        actual(list.get(0).get("C2").getClass().getCanonicalName()).should(equal("java.lang.Long"));
        // read-list-of-maps-header
    }

    @Test
    public void writeListOfMaps() {
        // list-data
        List<Map<String, ?>> list = Arrays.asList(
                aMapOf("colA", 1, "colB", "R1"),
                aMapOf("colA", 2, "colB", "R2"));
        // list-data

        // write-csv-list-maps
        Path path = data.csv.write("generated/from-list-maps.csv", list);
        // write-csv-list-maps
        actual(FileUtils.fileTextContent(path)).should(equal("colA,colB\r\n" +
                "1,R1\r\n" +
                "2,R2\r\n"));
    }

    @Test
    public void writeTable() {
        // table-data
        TableData csvTable = table("id"  , "value",
                                   ________________,
                                   "id1" , "value1",
                                   "id2" , "value2");
        // table-data

        // write-csv-table
        Path path = data.csv.write("generated/from-table-data.csv", csvTable);
        // write-csv-table

        actual(FileUtils.fileTextContent(path)).should(equal("id,value\r\n" +
            "id1,value1\r\n" +
            "id2,value2\r\n"));
    }
}
