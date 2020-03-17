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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.utils.CsvUtils;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Data {
    public static final Data data = new Data();

    private Data() {
    }

    public List<Map<String, String>> csv(String fileOrResourcePath) {
        return CsvUtils.parse(textContent(fileOrResourcePath));
    }

    public List<Map<String, Object>> csvAutoConverted(String fileOrResourcePath) {
        return CsvUtils.parseWithAutoConversion(textContent(fileOrResourcePath));
    }

    public List<Map<String, String>> csv(List<String> header, String fileOrResourcePath) {
        return CsvUtils.parse(header, textContent(fileOrResourcePath));
    }

    public List<Map<String, Object>> csvAutoConverted(List<String> header, String fileOrResourcePath) {
        return CsvUtils.parseWithAutoConversion(header, textContent(fileOrResourcePath));
    }

    private String textContent(String fileOrResourcePath) {
        Path filePath = WebTauConfig.getCfg().getWorkingDir().resolve(fileOrResourcePath);

        boolean hasResource = ResourceUtils.hasResource(fileOrResourcePath);
        boolean hasFile = Files.exists(filePath);

        if (!hasResource && ! hasFile) {
            throw new IllegalArgumentException("Can't find resource \"" + fileOrResourcePath + "\" or " +
                    "file \"" + filePath.toAbsolutePath() + "\"");
        }

        return hasResource ?
                ResourceUtils.textContent(fileOrResourcePath) :
                FileUtils.fileTextContent(filePath);
    }
}
