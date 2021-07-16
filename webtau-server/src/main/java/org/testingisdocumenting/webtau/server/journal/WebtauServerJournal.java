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

package org.testingisdocumenting.webtau.server.journal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * collects server calls; waits on calls
 */
public class WebtauServerJournal {
    private final List<WebtauServerHandledRequest> handledRequestList;

    public final WebtauServerJournalHandledRequests handledRequests;
    private final String serverId;

    public WebtauServerJournal(String serverId) {
        this.serverId = serverId;
        this.handledRequests = new WebtauServerJournalHandledRequests(this);
        this.handledRequestList = Collections.synchronizedList(new ArrayList<>());
    }

    public void registerCall(String method, String url, String contentType, long startTime, long elapsedTime) {
        handledRequestList.add(new WebtauServerHandledRequest(method.toUpperCase(), url, contentType, startTime, elapsedTime));
    }

    public List<WebtauServerHandledRequest> handledRequestsByMethod(String method) {
        return handledRequestList.stream()
                .filter(call -> call.getMethod().equals(method))
                .collect(Collectors.toList());
    }

    public String getServerId() {
        return serverId;
    }
}
