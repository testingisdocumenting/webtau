/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.db.gen;

import org.testingisdocumenting.webtau.data.table.Record;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlQueriesGenerator {
    private SqlQueriesGenerator() {
    }

    public static String insert(String tableName, Record record) {
        return insert(tableName, record.getHeader().getNamesStream(), record.valuesStream());
    }

    public static String insert(String tableName, Map<String, Object> row) {
        return insert(tableName, row.keySet().stream(), row.values().stream());
    }

    public static String insert(String tableName, Stream<String> columnNamesStream, Stream<?> valuesStream) {
        String enumeratedColumnNames = columnNamesStream.collect(Collectors.joining(", "));
        String questionMarks = valuesStream.map(v -> "?").collect(Collectors.joining(", "));

        return "INSERT INTO " + tableName + " (" + enumeratedColumnNames + ") VALUES (" + questionMarks + ")";
    }

    public static String fullTable(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String count(String tableName) {
        return "SELECT count(*) FROM " + tableName;
    }
}
