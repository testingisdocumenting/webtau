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

package org.testingisdocumenting.webtau.cache;

import java.nio.file.Path;

public class CachedValue<E> {
    private final Cache cache;
    private final String id;

    public CachedValue(Cache cache, String id) {
        this.cache = cache;
        this.id = id;
    }

    public E get() {
        return cache.get(id);
    }

    public boolean exists() {
        return cache.exists(id);
    }

    public Path getAsPath() {
        return cache.getAsPath(id);
    }

    public void set(E value) {
        cache.put(id, value);
    }
}
