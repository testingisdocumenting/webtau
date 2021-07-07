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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

class WebtauServerOverrideNoResponse implements WebtauServerOverride {
    static final String OVERRIDE_ID = "unresponsive";
    private final String serverId;
    private final Object sleepLock;

    WebtauServerOverrideNoResponse(String serverId) {
        this.serverId = serverId;
        this.sleepLock = ServerResponseWaitLocks.grabTimerLockByServerId(serverId);
    }

    @Override
    public boolean matchesUri(String method, String uri) {
        return true;
    }

    @Override
    public String overrideId() {
        return OVERRIDE_ID;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) throws IOException, ServletException {
        try {
            synchronized (sleepLock) {
                sleepLock.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new byte[0];
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return "NA";
    }
}
