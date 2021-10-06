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

import org.testingisdocumenting.webtau.reporter.WebTauStepPayload;

import java.util.*;
import java.util.stream.Collectors;

/**
 * collects server calls; waits on calls
 */
public class WebtauServerJournal implements WebTauStepPayload {
    private final List<WebtauServerHandledRequest> handledRequestList;
    private final ThreadLocal<Integer> testLocalRequestsStartIdx = ThreadLocal.withInitial(() -> 0);

    private final String serverId;

    public final WebtauServerJournalHandledRequests.HandledRequestsLiveValue GET;
    public final WebtauServerJournalHandledRequests.HandledRequestsLiveValue POST;
    public final WebtauServerJournalHandledRequests.HandledRequestsLiveValue PUT;
    public final WebtauServerJournalHandledRequests.HandledRequestsLiveValue DELETE;
    public final WebtauServerJournalHandledRequests.HandledRequestsLiveValue PATCH;

    public WebtauServerJournal(String serverId) {
        this.serverId = serverId;
        WebtauServerJournalHandledRequests handledRequests = new WebtauServerJournalHandledRequests(this);
        handledRequestList = Collections.synchronizedList(new ArrayList<>());
        GET = handledRequests.GET;
        POST = handledRequests.POST;
        PUT = handledRequests.PUT;
        DELETE = handledRequests.DELETE;
        PATCH = handledRequests.PATCH;
    }

    public void registerCall(WebtauServerHandledRequest handledRequest) {
        handledRequestList.add(handledRequest);
    }

    public WebtauServerHandledRequest getLastHandledRequest() {
        if (handledRequestList.isEmpty()) {
            return WebtauServerHandledRequest.NULL;
        }

        return handledRequestList.get(handledRequestList.size() - 1);
    }

    public List<WebtauServerHandledRequest> handledRequestsByMethod(String method) {
        return handledRequestList.stream()
                .filter(call -> call.getMethod().equals(method))
                .collect(Collectors.toList());
    }

    public String getServerId() {
        return serverId;
    }

    // each thread maintains a captured list
    // so each individual test can capture the outputs related to the test
    void resetTestLocalRequestsStartIdx() {
        testLocalRequestsStartIdx.set(handledRequestList.size());
    }

    @Override
    public Map<String, ?> toMap() {
        List<? extends Map<String, ?>> testLocalCalls = handledRequestList.subList(testLocalRequestsStartIdx.get(), handledRequestList.size())
                .stream()
                .map(WebtauServerHandledRequest::toMap)
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("serverId", serverId);
        result.put("capturedCalls", testLocalCalls);

        return result;
    }
}
