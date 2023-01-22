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
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.data.DataContentUtils.*;

public class DataJson {
    /**
     * Use <code>data.json.map</code> to read data as {@link java.util.Map} from JSON file.
     * <p>
     * Passed path is either relative based on working dir or absolute path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return parsed map
     */
    public Map<String, ?> map(String fileOrResourcePath) {
        return handleTextContentFromPath(DataPath.fromFileOrResourcePath(fileOrResourcePath), JsonUtils::deserializeAsMap);
    }

    /**
     * Use <code>data.json.map</code> to read data as {@link java.util.Map} from JSON file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path
     * @param filePath relative file path or absolute file path
     * @return parsed map
     */
    public Map<String, ?> map(Path filePath) {
        return handleTextContentFromPath(DataPath.fromFilePath(filePath), JsonUtils::deserializeAsMap);
    }

    /**
     * Use <code>data.json.mapFromString</code> to parse given JSON string as {@link java.util.Map}
     *
     * @param json JSON string representation
     * @return parsed map
     */
    public Map<String, ?> mapFromString(String json) {
        return handleTextContent(json, JsonUtils::deserializeAsMap);
    }

    /**
     * Use <code>data.json.list</code> to read data as {@link java.util.List} from JSON file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return parsed list
     */
    public <R> List<R> list(String fileOrResourcePath) {
        return handleTextContentFromPath(DataPath.fromFileOrResourcePath(fileOrResourcePath), DataJson::listFromJson);
    }

    /**
     * Use <code>data.json.list</code> to read data as {@link java.util.List} from JSON file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path
     * @param filePath relative file path or absolute file path
     * @return parsed list
     */
    public <R> List<R> list(Path filePath) {
        return handleTextContentFromPath(DataPath.fromFilePath(filePath), DataJson::listFromJson);
    }

    /**
     * Use <code>data.json.listFromString</code> to parse given JSON string as {@link java.util.List}
     *
     * @param json JSON string representation
     * @return parsed list
     */
    public <R> List<R> listFromString(String json) {
        return handleTextContent(json, DataJson::listFromJson);
    }

    /**
     * Use <code>data.json.table</code> to read data as {@link TableData} from JSON file. JSON must be a list.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return TableData of values
     */
    public TableData table(String fileOrResourcePath) {
        return handleTextContentFromPath(DataPath.fromFileOrResourcePath(fileOrResourcePath), DataJson::tableFromJsonList);
    }

    /**
     * Use <code>data.json.table</code> to read data as {@link TableData} from JSON file. JSON must be a list.
     * <p>
     * Passed path is either relative based on working dir or absolute file path
     * @param filePath relative file path or absolute file path
     * @return TableData of values
     */
    public TableData table(Path filePath) {
        return handleTextContentFromPath(DataPath.fromFilePath(filePath), DataJson::tableFromJsonList);
    }

    /**
     * Use <code>data.json.tableFromString</code> to parse given JSON string as {@link TableData}
     *
     * @param json JSON string representation
     * @return TableData of values
     */
    public TableData tableFromString(String json) {
        return handleTextContent(json, DataJson::tableFromJsonList);
    }

    /**
     * Use <code>data.json.object</code> to read data as either {@link java.util.List} or {@link java.util.Map} or a single value from JSON file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return parsed json as object
     */
    public Object object(String fileOrResourcePath) {
        return handleTextContentFromPath(DataPath.fromFileOrResourcePath(fileOrResourcePath), JsonUtils::deserialize);
    }

    /**
     * Use <code>data.json.objectFromString</code> to parse given JSON string as either {@link java.util.List} or {@link java.util.Map} or a single value from JSON file.
     *
     * @param json JSON string representation
     * @return parsed json as object
     */
    public Object objectFromString(String json) {
        return handleTextContent(json, JsonUtils::deserialize);
    }

    /**
     * Use <code>data.json.object</code> to read data as either {@link java.util.List} or  {@link java.util.Map} from JSON file.
     * <p>
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @return parsed json as object
     */
    public Object object(Path filePath) {
        return handleTextContentFromPath(DataPath.fromFilePath(filePath), JsonUtils::deserialize);
    }

    /**
     * Use <code>data.json.write</code> to write data to JSON file.
     * @param path relative path or absolute file path of file to create
     * @param tableData {@link TableData} to write as JSON
     * @return full path to a newly created file
     */
    public Path write(String path, TableData tableData) {
        return writeJsonContentAsStep(Paths.get(path), tableData::toJson);
    }

    /**
     * Use <code>data.json.write</code> to write data to JSON file.
     * @param path relative path or absolute file path of file to create
     * @param list list to write as JSON
     * @return full path to a newly created file
     */
    public Path write(String path, List<?> list) {
        return writeJsonContentAsStep(Paths.get(path), () -> JsonUtils.serializePrettyPrint(list));
    }

    /**
     * Use <code>data.json.write</code> to write data to JSON file.
     * @param path relative path or absolute file path of file to create
     * @param map map to write as JSON
     * @return full path to a newly created file
     */
    public Path write(String path, Map<String, ?> map) {
        return writeJsonContentAsStep(Paths.get(path), () -> JsonUtils.serializePrettyPrint(map));
    }

    private static Path writeJsonContentAsStep(Path path, Supplier<String> convertor) {
        return writeTextContentAsStep("json", path, convertor);
    }

    @SuppressWarnings("unchecked")
    private static <R> List<R> listFromJson(String json) {
        List<?> list = JsonUtils.deserializeAsList(json);
        return (List<R>) list;
    }

    @SuppressWarnings("unchecked")
    private static TableData tableFromJsonList(String json) {
        Object object = JsonUtils.deserialize(json);
        if (!(object instanceof List)) {
            throwOnlyJsonListOfObjectsCanBeConvertedToTable();
        }

        List<?> list = (List<?>) object;
        if (list.isEmpty()) {
            return new TableData(Collections.emptyList());
        }

        if (list.stream().anyMatch(v -> !(v instanceof Map))) {
           throwOnlyJsonListOfObjectsCanBeConvertedToTable();
        }

        return TableData.fromListOfMaps((List<Map<String, ?>>) list);
    }

    private static void throwOnlyJsonListOfObjectsCanBeConvertedToTable() {
        throw new IllegalArgumentException("only JSON list of objects can be converted to TableData");
    }

    private static <R> R handleTextContentFromPath(DataPath path, Function<String, R> convertor) {
        return readAndConvertTextContentFromDataPathAsStep("json", path, convertor);
    }

    private static <R> R handleTextContent(String content, Function<String, R> convertor) {
        return convertTextContent("json", content, convertor);
    }
}
