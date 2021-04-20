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

import java.util.*;
import java.util.stream.Collectors;

/**
 * manages performance histogram buckets
 *
 * @see OperationsPerformanceHistogramBucket
 */
public class OperationsPerformanceHistogram {
    private final Map<Long, OperationsPerformanceHistogramBucket> bucketPerMinMs;
    private final long stepMs;

    public OperationsPerformanceHistogram(long stepMs) {
        this.stepMs = stepMs;
        bucketPerMinMs = new TreeMap<>();
    }

    public void addOperation(OperationPerformance operation) {
        long assignedMinMs = calcBucketMinMs(operation.getElapsedMs());
        OperationsPerformanceHistogramBucket bucket = bucketPerMinMs.computeIfAbsent(assignedMinMs,
                (minMs) -> new OperationsPerformanceHistogramBucket(minMs, minMs + stepMs));

        bucket.addOperation(operation);
    }

    public Collection<OperationsPerformanceHistogramBucket> getBuckets() {
        return Collections.unmodifiableCollection(bucketPerMinMs.values());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("stepMs", stepMs);
        result.put("buckets", bucketPerMinMs.values()
                .stream()
                .map(OperationsPerformanceHistogramBucket::toMap)
                .collect(Collectors.toList()));

        return result;
    }

    long calcBucketMinMs(long elapsedMs) {
        return (elapsedMs / stepMs) * stepMs;
    }
}
