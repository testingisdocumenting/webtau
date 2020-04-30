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

import java.util.List;
import java.util.stream.Collectors;

public class SqlQueriesGenerator {
    private SqlQueriesGenerator() {
    }

    public static String insert(String tableName, Record record) {
        String columnNames = record.getHeader().getNamesStream().collect(Collectors.joining(", "));

        List<Object> values = record.getValues();
        String questionMarks = values.stream().map(v -> "?").collect(Collectors.joining(", "));

        return "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + questionMarks + ")";
    }

    public static String query(String tableName) {
        return "SELECT * FROM " + tableName;
    }
}
