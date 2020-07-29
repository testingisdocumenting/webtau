/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.documentation;

import org.testingisdocumenting.webtau.data.table.Record;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.utils.CsvUtils;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentationArtifacts {
    private static final ConcurrentHashMap<String, Boolean> usedArtifactNames = new ConcurrentHashMap<>();

    public static void registerName(String artifactName) {
        Boolean previous = usedArtifactNames.put(artifactName, true);
        if (previous != null) {
            throw new AssertionError("doc artifact name <" + artifactName + "> was already used");
        }
    }

    public static void clearRegisteredNames() {
        usedArtifactNames.clear();
    }

    public static void create(Class<?> testClass, String artifactName, String text) {
        Path path = DocumentationArtifactsLocation.classBasedLocation(testClass).resolve(artifactName);
        FileUtils.writeTextContent(path, text);
    }

    public static void createAsJson(Class<?> testClass, String artifactName, Object value) {
        artifactName += ".json";

        if (value instanceof TableData) {
            create(testClass, artifactName, JsonUtils.serializePrettyPrint(((TableData) value).toListOfMaps()));
        } else {
            create(testClass, artifactName, JsonUtils.serializePrettyPrint(value));
        }
    }

    public static void createAsCsv(Class<?> testClass, String artifactName, TableData tableData) {
        create(testClass, artifactName + ".csv", CsvUtils.serialize(
                tableData.getHeader().getNamesStream(),
                tableData.rowsStream().map(Record::getValues)));
    }
}
