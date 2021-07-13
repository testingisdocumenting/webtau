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

package org.testingisdocumenting.webtau.server;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class WebtauServerGlobalOverrides {
    private static final Map<String, WebtauServerOverride> contentOverrides = new ConcurrentHashMap<>();
    private static final Map<String, WebtauServerOverride> stateOverrides = new ConcurrentHashMap<>();

    static void addContentOverride(String serverId, WebtauServerOverride override) {
        addOverride(contentOverrides, serverId, override);
    }

    static void addStateOverride(String serverId, WebtauServerOverride override) {
        addOverride(stateOverrides, serverId, override);
    }

    static void removeOverride(String serverId, String overrideId) {
        String id = makeId(serverId, overrideId);
        contentOverrides.remove(id);
        stateOverrides.remove(id);
    }

    private static void addOverride(Map<String, WebtauServerOverride> overrides,
                                    String serverId,
                                    WebtauServerOverride override) {
        WebtauServerOverride existing = overrides.put(makeId(serverId, override.overrideId()), override);
        if (existing != null) {
            throw new RuntimeException("already found an override for server: " + serverId +
                    " with override id: " + override.overrideId() + ", existing override: " + existing);
        }
    }

    static Optional<WebtauServerOverride> findOverride(String serverId, String method, String uri) {
        return Stream.concat(stateOverrides.entrySet().stream(), contentOverrides.entrySet().stream())
                .filter(e -> e.getKey().startsWith(serverId + "."))
                .filter(e -> e.getValue().matchesUri(method, uri))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private static String makeId(String serverId, String overrideId) {
        return serverId + "." + overrideId;
    }
}
