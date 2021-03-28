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

package org.testingisdocumenting.webtau.openapi;

import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*;

class OpenApiSpecLocation {
    private final String originalValue;
    private final Path path;
    private final String url;

    private OpenApiSpecLocation(String originalValue, Path path, String url) {
        this.originalValue = originalValue;
        this.path = path;
        this.url = url;
    }

    public static OpenApiSpecLocation fromStringValue(String value) {
        if (value.isEmpty()) {
            return OpenApiSpecLocation.undefined();
        }

        if (value.startsWith("/")) {
            if (Files.exists(Paths.get(value))) {
                return OpenApiSpecLocation.fromFs(value, Paths.get(value));
            }

            return OpenApiSpecLocation.fromUrl(value, UrlUtils.concat(getCfg().getBaseUrl(), value));
        }

        if (UrlUtils.isFull(value)) {
            return OpenApiSpecLocation.fromUrl(value, value);
        }

        return OpenApiSpecLocation.fromFs(value, getCfg().getWorkingDir().resolve(value));
    }

    static OpenApiSpecLocation fromFs(String originalValue, Path path) {
        return new OpenApiSpecLocation(originalValue, path.toAbsolutePath(), null);
    }

    static OpenApiSpecLocation fromUrl(String originalValue, String url) {
        return new OpenApiSpecLocation(originalValue, null, url);
    }

    static OpenApiSpecLocation undefined() {
        return new OpenApiSpecLocation(null, null, null);
    }

    public Path getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public String getAsString() {
        return isFileSystem() ?
                path.toString():
                url;
    }

    public boolean isDefined() {
        return isFileSystem() || isUrl();
    }

    private boolean isUrl() {
        return url != null;
    }

    public boolean isFileSystem() {
        return path != null;
    }
}
