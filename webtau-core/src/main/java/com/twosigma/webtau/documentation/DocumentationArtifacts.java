/*
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

package com.twosigma.webtau.documentation;

import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.utils.CsvUtils;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;

import java.nio.file.Path;

public class DocumentationArtifacts {
    public static void create(Class<?> testClass, String artifactName, String text) {
        Path path = DocumentationArtifactsLocation.classBasedLocation(testClass).resolve(artifactName);
        FileUtils.writeTextContent(path, text);
    }

    public static void createAsJson(Class<?> testClass, String artifactName, TableData tableData) {
        create(testClass, artifactName, JsonUtils.serializePrettyPrint(tableData.toListOfMaps()));
    }

    public static void createAsCsv(Class<?> testClass, String artifactName, TableData tableData) {
        create(testClass, artifactName, CsvUtils.serialize(
                tableData.getHeader().getNamesStream(),
                tableData.rowsStream().map(Record::getValues)));
    }
}
