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

import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.testingisdocumenting.webtau.data.DataContentUtils.readAndConvertTextContentAsStep;

class DataJson {
    /**
     * Use <code>data.json.map</code> to read data as {@link java.util.Map} from JSON file.
     * Passed path is either relative based on working dir or absolute path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of primitive values or maps/list
     */
    public Map<String, ?> map(String fileOrResourcePath) {
        return handleTextContent(DataPath.fromFileOrResourcePath(fileOrResourcePath), JsonUtils::deserializeAsMap);
    }

    /**
     * Use <code>data.json.map</code> to read data as {@link java.util.Map} from JSON file.
     * Passed path is either relative based on working dir or absolute file path
     * @param filePath relative file path or absolute file path
     * @return list of primitive values or maps/list
     */
    public Map<String, ?> map(Path filePath) {
        return handleTextContent(DataPath.fromFilePath(filePath), JsonUtils::deserializeAsMap);
    }

    /**
     * Use <code>data.json.list</code> to read data as {@link java.util.List} from JSON file.
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of primitive values or maps/list
     */
    public List<?> list(String fileOrResourcePath) {
        return handleTextContent(DataPath.fromFileOrResourcePath(fileOrResourcePath), JsonUtils::deserializeAsList);
    }

    /**
     * Use <code>data.json.object</code> to read data as either {@link java.util.List} or  {@link java.util.Map} from JSON file.
     * Passed path is either relative based on working dir or absolute file path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return list of primitive values or maps/list
     */
    public Object object(String fileOrResourcePath) {
        return handleTextContent(DataPath.fromFileOrResourcePath(fileOrResourcePath), JsonUtils::deserialize);
    }

    /**
     * Use <code>data.json.object</code> to read data as either {@link java.util.List} or  {@link java.util.Map} from JSON file.
     * Passed path is either relative based on working dir or absolute file path.
     * @param filePath relative file path or absolute file path
     * @return list of primitive values or maps/list
     */
    public Object object(Path filePath) {
        return handleTextContent(DataPath.fromFilePath(filePath), JsonUtils::deserialize);
    }

    private static <R> R handleTextContent(DataPath path, Function<String, R> convertor) {
        return readAndConvertTextContentAsStep("json", path, convertor);
    }
}
