/*
 * Copyright 2021 webtau maintainers
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
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;

class DataPath {
    private final String fileOrResourcePath;
    private final Path fullFilePath;
    private final boolean isResource;
    private final boolean isFile;

    private final String givenPathAsString;
    private final String resolvedPathAsString;

    static DataPath fromFileOrResourcePath(String fileOrResourcePath) {
        return new DataPath(fileOrResourcePath, null);
    }

    static DataPath fromFilePath(Path path) {
        return new DataPath(null, path);
    }

    DataPath(String fileOrResourcePath, Path filePath) {
        if (fileOrResourcePath == null && filePath == null) {
            throw new IllegalArgumentException("one of the paths needs to be specified");
        }

        this.fileOrResourcePath = fileOrResourcePath;
        this.fullFilePath = filePath != null ?
                WebTauConfig.getCfg().fullPath(filePath):
                WebTauConfig.getCfg().fullPath(fileOrResourcePath);

        this.isResource = fileOrResourcePath != null && ResourceUtils.hasResource(fileOrResourcePath);

        this.isFile = Files.exists(fullFilePath);

        this.givenPathAsString = fileOrResourcePath != null ?
                fileOrResourcePath :
                filePath.toString();

        this.resolvedPathAsString = isResource ?
                fileOrResourcePath :
                fullFilePath.toString();
    }

    public String getGivenPathAsString() {
        return givenPathAsString;
    }

    public String getResolvedPathAsString() {
        return resolvedPathAsString;
    }

    public String getFileOrResourcePath() {
        return fileOrResourcePath;
    }

    public Path getFullFilePath() {
        return fullFilePath;
    }

    public boolean isResourceSpecified() {
        return fileOrResourcePath != null;
    }

    public boolean isResource() {
        return isResource;
    }

    public boolean isFile() {
        return isFile;
    }
}
