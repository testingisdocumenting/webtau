/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.data.csv;

import com.twosigma.webtau.utils.StringUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class CsvParser {
    private CsvParser() {
    }

    public static List<Map<String, Object>> parse(String content) {
        return parse(Collections.emptyList(), content);
    }

    public static List<Map<String, Object>> parse(List<String> header, String content) {
        List<Map<String, Object>> tableData = new ArrayList<>();

        CSVParser csvRecords = readCsvRecords(header, content);

        Collection<String> headerToUse = header.isEmpty() ?
                csvRecords.getHeaderMap().keySet() :
                header;

        for (CSVRecord record : csvRecords) {
            Map<String, Object> row = convertCsvRecord(headerToUse, record);
            tableData.add(row);
        }

        return tableData;
    }

    private static CSVParser readCsvRecords(List<String> header, String content) {
        try {
            CSVFormat csvFormat = CSVFormat.RFC4180;
            if (header.isEmpty()) {
                csvFormat = csvFormat.withFirstRecordAsHeader();
            }

            return csvFormat.
                    withIgnoreSurroundingSpaces().
                    withIgnoreEmptyLines().
                    withTrim().
                    withDelimiter(',').
                    parse(new StringReader(content));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> convertCsvRecord(Collection<String> header, CSVRecord record) {
        Map<String, Object> row = new LinkedHashMap<>();

        int idx = 0;
        for (String columnName : header) {
            row.put(columnName, convertValue(record.get(idx)));
            idx++;
        }

        return row;
    }

    private static Object convertValue(Object v) {
        String s = v.toString();
        return StringUtils.isNumeric(s) ? StringUtils.convertToNumber(s) : s;
    }
}
