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
import java.util.concurrent.ConcurrentHashMap;

/**
 * manages response sleeps and release on server shutdown
 */
class ServerResponseWaitLocks {
    private static final Map<String, Object> timerLockByServerId = new ConcurrentHashMap<>();

    static Object grabTimerLockByServerId(String serverId) {
        return timerLockByServerId.computeIfAbsent(serverId, (id) -> new Object());
    }

    static void releaseLock(String serverId) {
        Object lock = timerLockByServerId.get(serverId);
        if (lock == null) {
            return;
        }

        synchronized (lock) {
            lock.notify();
        }
    }
}
