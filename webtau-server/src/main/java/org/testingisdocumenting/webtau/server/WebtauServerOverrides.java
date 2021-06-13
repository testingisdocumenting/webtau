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

class WebtauServerOverrides {
    private static final Map<String, WebtauServerOverride> overrides = new ConcurrentHashMap<>();

    static void addOverride(String serverId, String overrideId, WebtauServerOverride override) {
        overrides.put(makeId(serverId, overrideId), override);
    }

    static void removeOverride(String serverId, String overrideId) {
        overrides.remove(makeId(serverId, overrideId));
    }

    static Optional<WebtauServerOverride> findOverride(String serverId, String uri) {
        return overrides.entrySet().stream()
                .filter(e -> e.getKey().startsWith(serverId + "."))
                .filter(e -> e.getValue().matchesUri(uri))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private static String makeId(String serverId, String overrideId) {
        return serverId + "." + overrideId;
    }
}
