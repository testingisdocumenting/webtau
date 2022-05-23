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

package org.testingisdocumenting.webtau.server.registry;

import org.testingisdocumenting.webtau.TestListener;
import org.testingisdocumenting.webtau.cleanup.CleanupRegistration;
import org.testingisdocumenting.webtau.reporter.TestResultPayload;
import org.testingisdocumenting.webtau.reporter.WebTauTest;
import org.testingisdocumenting.webtau.server.WebTauServer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WebTauServersRegistry implements TestListener {
    private static final Map<String, WebTauServer> serverById = new ConcurrentHashMap<>();

    static {
        registerCleanup();
    }

    public static boolean hasServerWithId(String id) {
        return serverById.containsKey(id);
    }

    public static void register(WebTauServer server) {
        serverById.put(server.getId(), server);
    }

    public static void validateId(String id) {
        if (WebTauServersRegistry.hasServerWithId(id)) {
            throw new IllegalArgumentException("server with <" + id + "> already exists");
        }
    }

    public static void unregister(WebTauServer server) {
        serverById.remove(server.getId());
    }

    @Override
    public void beforeTestRun(WebTauTest test) {
        serverById.values().forEach(server -> server.getJournal().resetTestLocalRequestsStartIdx());
    }

    @Override
    public void afterTestRun(WebTauTest test) {
        List<Map<String, ?>> serverJournals = serverById.values()
                .stream()
                .map(server -> server.getJournal().toMap())
                .filter(journalMap -> !((List<?>) journalMap.get("capturedCalls")).isEmpty())
                .collect(Collectors.toList());

        test.addTestResultPayload(new TestResultPayload("servers", serverJournals));
    }

    private static void stopServers() {
        serverById.values().stream()
                .filter(WebTauServer::isRunning)
                .forEach(WebTauServer::stop);
        serverById.clear();
    }

    private static void registerCleanup() {
        CleanupRegistration.registerForCleanup("stopping", "stopped", "servers",
                () -> serverById.values().stream().anyMatch(WebTauServer::isRunning),
                WebTauServersRegistry::stopServers);
    }
}
