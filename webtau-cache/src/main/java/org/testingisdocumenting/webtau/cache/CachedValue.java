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

public class CachedValue<E> {
    private final Cache cache;
    private final String id;
    private E value;

    public CachedValue(Cache cache, String id) {
        this.cache = cache;
        this.id = id;
        this.value = cache.get(id);
    }

    public E get() {
        return value;
    }

    public void set(E value) {
        this.value = value;
        cache.put(id, value);
    }
}
