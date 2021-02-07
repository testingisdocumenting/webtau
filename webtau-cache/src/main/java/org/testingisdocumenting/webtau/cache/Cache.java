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

import org.testingisdocumenting.webtau.cfg.WebTauConfig;

public class Cache {
    public static final Cache cache = new Cache();

    private final FileBasedCache fileBasedCache;

    private Cache() {
        fileBasedCache = new FileBasedCache(() -> WebTauConfig.getCfg().getCachePath());
    }

    public <E> CachedValue<E> value(String id) {
        return new CachedValue<>(cache, id);
    }

    public <E> E get(String key) {
        return fileBasedCache.get(key);
    }

    public void put(String key, Object value, long expirationTime) {
        fileBasedCache.put(key, value, expirationTime);
    }

    public void put(String key, Object value) {
        fileBasedCache.put(key, value);
    }
}
