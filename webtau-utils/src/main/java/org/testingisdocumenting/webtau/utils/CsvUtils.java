/*
 * Copyright 2021 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CsvUtils {
    private CsvUtils() {
    }

    public static List<Map<String, String>> parse(String content) {
        return parse(Collections.emptyList(), content);
    }

    public static List<Map<String, Object>> parseWithAutoConversion(String content) {
        return convertValues(parse(content));
    }

    public static List<Map<String, String>> parse(List<String> header, String content) {
        List<Map<String, String>> tableData = new ArrayList<>();

        CSVParser csvRecords = readCsvRecords(header, content);

        Collection<String> headerToUse = header.isEmpty() ?
                csvRecords.getHeaderMap().keySet() :
                header;

        for (CSVRecord record : csvRecords) {
            tableData.add(createRow(headerToUse, record));
        }

        return tableData;
    }

    public static List<Map<String, Object>> parseWithAutoConversion(List<String> header, String content) {
        return convertValues(parse(header, content));
    }

    public static String serialize(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            return "";
        }

        return CsvUtils.serialize(
                rows.get(0).keySet().stream(),
                rows.stream().map(Map::values));
    }

    public static String serialize(Stream<String> header, Stream<Collection<Object>> rows) {
        try {
            StringWriter out = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(header.toArray(String[]::new)));

            Iterator<Collection<Object>> it = rows.iterator();
            while (it.hasNext()) {
                csvPrinter.printRecord(it.next());
            }

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static Map<String, String> createRow(Collection<String> header, CSVRecord record) {
        Map<String, String> row = new LinkedHashMap<>();

        int idx = 0;
        try {
            for (String columnName : header) {
                String value = record.get(idx);
                row.put(columnName, value);
                idx++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't parse " + record, e);
        }

        return row;
    }

    private static List<Map<String, Object>> convertValues(List<Map<String, String>> data) {
        return data.stream().map(CsvUtils::convertRecord).collect(toList());
    }

    private static Map<String, Object> convertRecord(Map<String, String> row) {
        Map<String, Object> entry = new LinkedHashMap<>();
        row.forEach((k, v) -> entry.put(k, convertValue(v)));

        return entry;
    }

    private static Object convertValue(Object v) {
        String s = v.toString();
        return StringUtils.isNumeric(s) ? StringUtils.convertToNumber(s) : s;
    }
}
