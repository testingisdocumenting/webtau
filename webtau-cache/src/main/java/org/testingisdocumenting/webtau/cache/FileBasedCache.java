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

package org.testingisdocumenting.webtau.cache;

import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

class FileBasedCache {
    private final Supplier<Path> cachePathSupplier;

    /**
     * @param cachePathSupplier path supplier, used instead of a direct value to avoid config trigger load
     */
    public FileBasedCache(Supplier<Path> cachePathSupplier) {
        this.cachePathSupplier = cachePathSupplier;
    }

    public boolean exists(String key) {
        Path valuePath = valueFilePathByKeyAndCreateDirIfRequired(key);
        return Files.exists(valuePath);
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String key) {
        Path valuePath = valueFilePathByKeyAndCreateDirIfRequired(key);
        if (!exists(key)) {
            return null;
        }

        return (E) JsonUtils.deserialize(FileUtils.fileTextContent(valuePath));
    }

    public void put(String key, Object value) {
        Path valuePath = valueFilePathByKeyAndCreateDirIfRequired(key);
        try {
            Files.write(valuePath, JsonUtils.serializePrettyPrint(value).getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path valueFilePathByKeyAndCreateDirIfRequired(String key) {
        Path root = cachePathSupplier.get();
        if (!Files.exists(root)) {
            FileUtils.createDirs(root);
        }

        return root.resolve(key + ".json");
    }
}
