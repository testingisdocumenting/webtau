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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.utils.CsvUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.*;
import static org.testingisdocumenting.webtau.data.DataContentUtils.*;

public class DataCsv {
    /**
     * Use <code>data.csv.table</code> to read data as {@link TableData} from CSV file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return table data with CSV content
     */
    public TableData table(String fileOrResourcePath) {
        return parseCsvTextAsStep(DataPath.fromFileOrResourcePath(fileOrResourcePath),
                (text) -> tableFromListOfMaps(CsvUtils.parse(text)));
    }

    /**
     * Use <code>data.csv.table</code> to read data as {@link TableData} from CSV file. It will convert values based on provided converter.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @param valueConverter converter to convert values from String
     * @return table data with CSV content
     */
    public TableData table(String fileOrResourcePath, DataCsvValueConverter valueConverter) {
        return parseCsvTextAsStep(DataPath.fromFileOrResourcePath(fileOrResourcePath),
                (text) -> tableFromListOfMaps(parseAndCovertValues(valueConverter, text)));
    }

    /**
     * Use <code>data.csv.table</code> to read data as {@link TableData} from CSV file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @return table data with CSV content
     */
    public TableData table(Path filePath) {
        return parseCsvTextAsStep(DataPath.fromFilePath(filePath),
                (text) -> tableFromListOfMaps(CsvUtils.parse(text)));
    }

    /**
     * Use <code>data.csv.table</code> to read data as {@link TableData} from CSV file. It will convert values based on provided converter.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @param valueConverter converter to convert values from String
     * @return table data with CSV content
     */
    public TableData table(Path filePath, DataCsvValueConverter valueConverter) {
        return parseCsvTextAsStep(DataPath.fromFilePath(filePath),
                (text) -> tableFromListOfMaps(parseAndCovertValues(valueConverter, text)));
    }

    /**
     * Use <code>data.csv.tableAutoConverted</code> to read data as {@link TableData} from CSV file. Numeric values become values of Numeric type instead of String type.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return table data with CSV content
     */
    public TableData tableAutoConverted(String fileOrResourcePath) {
        return table(fileOrResourcePath, new DataCsvAutoNumberConversion());
    }

    /**
     * Use <code>data.csv.tableAutoConverted</code> to read data as {@link TableData} from CSV file. Numeric values become values of Numeric type instead of String type.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @return table data with CSV content
     */
    public TableData tableAutoConverted(Path filePath) {
        return table(filePath, new DataCsvAutoNumberConversion());
    }

    /**
     * Use <code>data.csv.listOfMaps</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of maps
     */
    public List<Map<String, String>> listOfMaps(String fileOrResourcePath) {
        return parseCsvTextAsStep(DataPath.fromFileOrResourcePath(fileOrResourcePath), CsvUtils::parse);
    }

    /**
     * Use <code>data.csv.listOfMaps</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @return list of maps
     */
    public List<Map<String, String>> listOfMaps(Path filePath) {
        return parseCsvTextAsStep(DataPath.fromFilePath(filePath), CsvUtils::parse);
    }

    /**
     * Use <code>data.csv.listOfMaps</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file. It will convert values based on provided converter.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @param valueConverter converter to convert values from String
     * @return list of maps
     */
    public List<Map<String, ?>> listOfMaps(String fileOrResourcePath, DataCsvValueConverter valueConverter) {
        return parseCsvTextAsStep(DataPath.fromFileOrResourcePath(fileOrResourcePath), (csv) -> parseAndCovertValues(valueConverter, csv));
    }

    /**
     * Use <code>data.csv.listOfMaps</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file. It will convert values based on provided converter.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @param valueConverter converter to convert values from String
     * @return list of maps
     */
    public List<Map<String, ?>> listOfMaps(Path filePath, DataCsvValueConverter valueConverter) {
        return parseCsvTextAsStep(DataPath.fromFilePath(filePath), (csv) -> parseAndCovertValues(valueConverter, csv));
    }

    /**
     * Use <code>data.csv.listOfMapsAutoConverted</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * Numeric values become values of Numeric type instead of String type.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of maps
     */
    public List<Map<String, ?>> listOfMapsAutoConverted(String fileOrResourcePath) {
        return listOfMaps(fileOrResourcePath, new DataCsvAutoNumberConversion());
    }

    /**
     * Use <code>data.csv.listOfMapsAutoConverted</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * Numeric values become values of Numeric type instead of String type.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @return list of maps
     */
    public List<Map<String, ?>> listOfMapsAutoConverted(Path filePath) {
        return listOfMaps(filePath, new DataCsvAutoNumberConversion());
    }

    /**
     * Use <code>data.csv.listOfMaps(header, path)</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * <p>
     * Header will be taken from first parameter and first row of CSV file will not be treated as header.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param header header values
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of maps
     */
    public List<Map<String, String>> listOfMaps(Collection<String> header, String fileOrResourcePath) {
        return parseCsvTextAsStep(DataPath.fromFileOrResourcePath(fileOrResourcePath),
                (text) -> CsvUtils.parse(header, text));
    }

    /**
     * Use <code>data.csv.listOfMaps(header, path)</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * <p>
     * Header will be taken from first parameter and first row of CSV file will not be treated as header.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param header header values
     * @param filePath relative file path or absolute file path
     * @return list of maps
     */
    public List<Map<String, String>> listOfMaps(Collection<String> header, Path filePath) {
        return parseCsvTextAsStep(DataPath.fromFilePath(filePath),
                (text) -> CsvUtils.parse(header, text));
    }

    /**
     * Use <code>data.csv.listOfMapsAutoConverted(header, path)</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * <p>
     * Header will be taken from first parameter and first row of CSV file will not be treated as header.
     * Numeric values become values of Numeric type instead of String type.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param header header values
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of maps
     */
    public List<Map<String, ?>> listOfMapsAutoConverted(List<String> header, String fileOrResourcePath) {
        return parseCsvTextAsStep(DataPath.fromFileOrResourcePath(fileOrResourcePath),
                (text) -> convertValues(new DataCsvAutoNumberConversion(), CsvUtils.parse(header, text)));
    }

    /**
     * Use <code>data.csv.listOfMapsAutoConverted(header, path)</code> to read data as {@link java.util.List} of {@link java.util.Map} from CSV file.
     * Header will be taken from first parameter and first row of CSV file will not be treated as header.
     * Numeric values become values of Numeric type instead of String type.
     * Passed path is either relative based on working dir or absolute file path.
     * @param header header values
     * @param filePath relative file path or absolute file path
     * @return list of maps
     */
    public List<Map<String, ?>> listOfMapsAutoConverted(List<String> header, Path filePath) {
        return parseCsvTextAsStep(DataPath.fromFilePath(filePath),
                (text) -> convertValues(new DataCsvAutoNumberConversion(), CsvUtils.parse(header, text)));
    }

    /**
     * Use <code>data.csv.write</code> to write data to CSV file.
     * @param path relative path or absolute file path of file to create
     * @param rows list of maps to write as CSV
     * @return full path to a newly created file
     */
    public Path write(Path path, List<Map<String, ?>> rows) {
        return writeCsvContentAsStep(path, () -> CsvUtils.serialize(rows));
    }

    /**
     * Use <code>data.csv.write</code> to write data to CSV file.
     * @param path relative path or absolute file path of file to create
     * @param rows list of maps to write as CSV
     * @return full path to a newly created file
     */
    public Path write(String path, List<Map<String, ?>> rows) {
        return writeCsvContentAsStep(Paths.get(path), () -> CsvUtils.serialize(rows));
    }

    /**
     * Use <code>data.csv.write</code> to write data to CSV file.
     * @param path relative path or absolute file path of file to create
     * @param tableData {@link TableData} to write as CSV
     * @return full path to a newly created file
     */
    public Path write(String path, TableData tableData) {
        return writeCsvContentAsStep(Paths.get(path), tableData::toCsv);
    }

    private static <R> R parseCsvTextAsStep(DataPath dataPath, Function<String, R> convertor) {
        return readAndConvertTextContentFromDataPathAsStep("csv", dataPath, convertor);
    }

    private static Path writeCsvContentAsStep(Path path, Supplier<String> convertor) {
        return writeTextContentAsStep("csv", path, convertor);
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

    private static List<Map<String, ?>> parseAndCovertValues(DataCsvValueConverter converter, String csv) {
        List<Map<String, String>> listOfMaps = CsvUtils.parse(csv);
        return convertValues(converter, listOfMaps);
    }

    private static List<Map<String, ?>> convertValues(DataCsvValueConverter converter, List<Map<String, String>> data) {
        return data.stream().map((e) -> convertRecord(converter, e)).collect(toList());
    }

    private static Map<String, Object> convertRecord(DataCsvValueConverter converter, Map<String, String> row) {
        Map<String, Object> entry = new LinkedHashMap<>();
        row.forEach((k, v) -> entry.put(k, converter.convert(k, ((Object) v).toString())));

        return entry;
    }
}
