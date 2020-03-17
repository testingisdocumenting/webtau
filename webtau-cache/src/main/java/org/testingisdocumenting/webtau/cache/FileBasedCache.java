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

package org.testingisdocumenting.webtau.cache;

import org.testingisdocumenting.webtau.time.Time;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FileBasedCache {
    private static final String VALUE_KEY = "value";
    private static final String EXPIRATION_TIME_KEY = "expirationTime";

    private final Supplier<Path> cachePathSupplier;
    private Map<String, Object> loadedCache;

    public FileBasedCache(Supplier<Path> cachePathSupplier) {
        this.cachePathSupplier = cachePathSupplier;
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String key) {
        loadCacheIfRequired();
        Map<String, Object> valueWithMeta = (Map<String, Object>) loadedCache.get(key);
        if (valueWithMeta == null) {
            return null;
        }

        Number expirationTime = (Number) valueWithMeta.get(EXPIRATION_TIME_KEY);
        if (Time.currentTimeMillis() >= expirationTime.longValue()) {
            return null;
        }

        return (E) valueWithMeta.get(VALUE_KEY);
    }

    public void put(String key, Object value, long expirationTime) {
        loadCacheIfRequired();
        Map<String, Object> valueWithMeta = createValueWithMeta(value, expirationTime);
        loadedCache.put(key, valueWithMeta);

        FileUtils.writeTextContent(cachePathSupplier.get(), JsonUtils.serializePrettyPrint(loadedCache));
    }

    public void put(String key, Object value) {
        put(key, value, Long.MAX_VALUE);
    }

    private Map<String, Object> createValueWithMeta(Object value, long expirationTime) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(VALUE_KEY, value);
        result.put(EXPIRATION_TIME_KEY, expirationTime);

        return result;
    }

    @SuppressWarnings("unchecked")
    private void loadCacheIfRequired() {
        if (loadedCache != null) {
            return;
        }

        Path cachePath = cachePathSupplier.get();
        if (!Files.exists(cachePath)) {
            loadedCache = new LinkedHashMap<>();
            return;
        }

        loadedCache = (Map<String, Object>) JsonUtils.deserialize(FileUtils.fileTextContent(cachePath));
    }
}
