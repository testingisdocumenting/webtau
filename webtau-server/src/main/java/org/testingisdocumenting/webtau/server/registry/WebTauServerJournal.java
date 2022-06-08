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
public class WebTauServerJournal implements WebTauStepPayload {
    private final List<WebTauServerHandledRequest> handledRequestList;
    private final ThreadLocal<Integer> testLocalRequestsStartIdx = ThreadLocal.withInitial(() -> 0);

    private final String serverId;

    public final WebTauServerJournalHandledRequests.HandledRequestsLiveValue GET;
    public final WebTauServerJournalHandledRequests.HandledRequestsLiveValue POST;
    public final WebTauServerJournalHandledRequests.HandledRequestsLiveValue PUT;
    public final WebTauServerJournalHandledRequests.HandledRequestsLiveValue DELETE;
    public final WebTauServerJournalHandledRequests.HandledRequestsLiveValue PATCH;

    public WebTauServerJournal(String serverId) {
        this.serverId = serverId;
        WebTauServerJournalHandledRequests handledRequests = new WebTauServerJournalHandledRequests(this);
        handledRequestList = Collections.synchronizedList(new ArrayList<>());
        GET = handledRequests.GET;
        POST = handledRequests.POST;
        PUT = handledRequests.PUT;
        DELETE = handledRequests.DELETE;
        PATCH = handledRequests.PATCH;
    }

    public void registerCall(WebTauServerHandledRequest handledRequest) {
        handledRequestList.add(handledRequest);
    }

    public WebTauServerHandledRequest getLastHandledRequest() {
        if (handledRequestList.isEmpty()) {
            return WebTauServerHandledRequest.NULL;
        }

        return handledRequestList.get(handledRequestList.size() - 1);
    }

    public List<WebTauServerHandledRequest> handledRequestsByMethod(String method) {
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
                .map(WebTauServerHandledRequest::toMap)
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("serverId", serverId);
        result.put("capturedCalls", testLocalCalls);

        return result;
    }
}
