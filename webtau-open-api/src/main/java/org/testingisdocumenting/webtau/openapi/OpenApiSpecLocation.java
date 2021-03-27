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

import java.nio.file.Path;

class OpenApiSpecLocation {
    private final Path path;
    private final String url;

    private OpenApiSpecLocation(Path path, String url) {
        this.path = path;
        this.url = url;
    }

    public static OpenApiSpecLocation fromFs(Path path) {
        return new OpenApiSpecLocation(path, null);
    }

    public static OpenApiSpecLocation fromUrl(String url) {
        return new OpenApiSpecLocation(null, url);
    }

    public static OpenApiSpecLocation undefined() {
        return new OpenApiSpecLocation(null, null);
    }

    public Path getPath() {
        return path;
    }

    public String getUrl() {
        return url;
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
