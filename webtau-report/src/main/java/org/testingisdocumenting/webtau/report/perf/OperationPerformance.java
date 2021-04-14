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

package org.testingisdocumenting.webtau.report.perf;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * single operation performance, e.g. HTTP call
 */
public class OperationPerformance {
    private final String uniqueId;
    private final String groupId;
    private final String operationId;
    private final long startTime;
    private final long elapsedMs;

    public OperationPerformance(String groupId, String operationId, long startTime, long elapsedMs) {
        this.uniqueId = UUID.randomUUID().toString();
        this.groupId = groupId;
        this.operationId = operationId;
        this.startTime = startTime;
        this.elapsedMs = elapsedMs;
    }

    /**
     * unique id to join in report by
     * @return unique id
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * group operation belongs to. e.g. Open API operation
     * @return operation group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * operation that was measured (e.g. HTTP call)
     * @return operation id
     */
    public String getOperationId() {
        return operationId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("groupId", groupId);
        result.put("operationId", operationId);
        result.put("startTime", startTime);
        result.put("elapsedMs", elapsedMs);

        return result;
    }
}
