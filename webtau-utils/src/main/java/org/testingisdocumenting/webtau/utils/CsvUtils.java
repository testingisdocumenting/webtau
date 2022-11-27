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
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Stream;

public class CsvUtils {
    private CsvUtils() {
    }

    public static List<Map<String, String>> parse(String content) {
        return parse(Collections.emptyList(), content);
    }

    public static List<Map<String, String>> parse(Collection<String> header, String content) {
        List<Map<String, String>> tableData = new ArrayList<>();

        try (CSVParser csvRecords = readCsvRecords(header, content)) {
            Collection<String> headerToUse = header.isEmpty() ?
                    csvRecords.getHeaderMap().keySet() :
                    header;

            for (CSVRecord record : csvRecords) {
                tableData.add(createRow(headerToUse, record));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return tableData;
    }

    public static String serialize(List<Map<String, ?>> rows) {
        if (rows.isEmpty()) {
            return "";
        }

        return CsvUtils.serialize(
                rows.get(0).keySet().stream(),
                rows.stream().map(Map::values));
    }

    public static String serialize(Stream<String> header, Stream<Collection<?>> rows) {
        try {
            StringWriter out = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(header.toArray(String[]::new)));

            Iterator<Collection<?>> it = rows.iterator();
            while (it.hasNext()) {
                csvPrinter.printRecord(it.next());
            }

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CSVParser readCsvRecords(Collection<String> header, String content) throws IOException {
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
}
