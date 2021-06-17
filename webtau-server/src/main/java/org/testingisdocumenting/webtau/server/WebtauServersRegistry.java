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

import org.testingisdocumenting.webtau.cleanup.CleanupRegistration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebtauServersRegistry {
    private static final Map<String, WebtauServer> serverById = new ConcurrentHashMap<>();

    static {
        registerCleanup();
    }

    public static boolean hasServerWithId(String id) {
        return serverById.containsKey(id);
    }

    public static void register(WebtauServer server) {
        serverById.put(server.getId(), server);
    }

    public static void validateId(String id) {
        if (WebtauServersRegistry.hasServerWithId(id)) {
            throw new IllegalArgumentException("server with <" + id + "> already exists");
        }
    }

    public static void unregister(WebtauServer server) {
        serverById.remove(server.getId());
    }

    private static void stopServers() {
        serverById.values().forEach(WebtauServer::stop);
        serverById.clear();
    }

    private static void registerCleanup() {
        CleanupRegistration.registerForCleanup("stopping", "stopped", "servers",
                () -> !serverById.isEmpty(),
                WebtauServersRegistry::stopServers);
    }
}
