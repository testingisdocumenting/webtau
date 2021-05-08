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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * holds list of operations for a specific latency time range
 */
public class OperationsPerformanceHistogramBucket {
    private final long minMsInclusive;
    private final long maxMsExclusive;

    private final List<String> operationUniqueIds;

    public OperationsPerformanceHistogramBucket(long minMsInclusive, long maxMsExclusive) {
        this.minMsInclusive = minMsInclusive;
        this.maxMsExclusive = maxMsExclusive;
        this.operationUniqueIds = new ArrayList<>();
    }

    void addOperation(OperationPerformance operation) {
        operationUniqueIds.add(operation.getUniqueId());
    }

    public long getMinMsInclusive() {
        return minMsInclusive;
    }

    public long getMaxMsExclusive() {
        return maxMsExclusive;
    }

    public List<String> getOperationUniqueIds() {
        return operationUniqueIds;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("minMsInclusive", minMsInclusive);
        result.put("maxMsExclusive", maxMsExclusive);
        result.put("operationUniqueIds", operationUniqueIds);

        return result;
    }
}
