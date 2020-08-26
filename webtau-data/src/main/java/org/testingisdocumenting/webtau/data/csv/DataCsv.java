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

package org.testingisdocumenting.webtau.data.csv;

import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.utils.CsvUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.data.DataContentUtils.dataTextContent;

public class DataCsv {
    public TableData table(String fileOrResourcePath) {
        return tableFromListOfMaps(CsvUtils.parse(dataTextContent(fileOrResourcePath)));
    }

    public TableData tableAutoConverted(String fileOrResourcePath) {
        return tableFromListOfMaps(CsvUtils.parseWithAutoConversion(dataTextContent(fileOrResourcePath)));
    }

    public List<Map<String, String>> listOfMaps(String fileOrResourcePath) {
        return CsvUtils.parse(dataTextContent(fileOrResourcePath));
    }

    public List<Map<String, Object>> listOfMapsAutoConverted(String fileOrResourcePath) {
        return CsvUtils.parseWithAutoConversion(dataTextContent(fileOrResourcePath));
    }

    public List<Map<String, String>> listOfMaps(List<String> header, String fileOrResourcePath) {
        return CsvUtils.parse(header, dataTextContent(fileOrResourcePath));
    }

    public List<Map<String, Object>> listOfMapsAutoConverted(List<String> header, String fileOrResourcePath) {
        return CsvUtils.parseWithAutoConversion(header, dataTextContent(fileOrResourcePath));
    }

    @SuppressWarnings("unchecked")
    private TableData tableFromListOfMaps(List<?> listOfMaps) {
        if (listOfMaps.isEmpty()) {
            return new TableData(Collections.emptyList());
        }

        Map<String, Object> firstRow = (Map<String, Object>) listOfMaps.get(0);

        TableData result = new TableData(firstRow.keySet().stream());
        listOfMaps.forEach((row) -> {
            Map<String, Object> asMap = (Map<String, Object>) row;
            result.addRow(asMap.values().stream());
        });

        return result;
    }
}
