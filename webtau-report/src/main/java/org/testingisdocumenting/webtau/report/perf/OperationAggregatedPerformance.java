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

public class OperationAggregatedPerformance {
    private String groupId;
    private long count;

    private double averageMs;
    private long minMs;
    private long maxMs;
    private double p20ms;
    private double p50ms;
    private double p80ms;
    private double p95ms;
    private double p99ms;

    public String getGroupId() {
        return groupId;
    }

    void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getCount() {
        return count;
    }

    void setCount(long count) {
        this.count = count;
    }

    public double getAverageMs() {
        return averageMs;
    }

    public long getMinMs() {
        return minMs;
    }

    void setMinMs(long minMs) {
        this.minMs = minMs;
    }

    public long getMaxMs() {
        return maxMs;
    }

    void setMaxMs(long maxMs) {
        this.maxMs = maxMs;
    }

    void setAverageMs(double averageMs) {
        this.averageMs = averageMs;
    }

    public double getP50ms() {
        return p50ms;
    }

    void setP50ms(double medianMs) {
        this.p50ms = medianMs;
    }

    public double getP20ms() {
        return p20ms;
    }

    void setP20ms(double p20ms) {
        this.p20ms = p20ms;
    }

    public double getP80ms() {
        return p80ms;
    }

    void setP80ms(double p80ms) {
        this.p80ms = p80ms;
    }

    public double getP95ms() {
        return p95ms;
    }

    void setP95ms(double p95ms) {
        this.p95ms = p95ms;
    }

    public double getP99ms() {
        return p99ms;
    }

    void setP99ms(double p99ms) {
        this.p99ms = p99ms;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("groupId", groupId);
        result.put("count", count);
        result.put("averageMs", averageMs);
        result.put("minMs", minMs);
        result.put("maxMs", maxMs);
        result.put("p20ms", p20ms);
        result.put("p50ms", p50ms);
        result.put("p80ms", p80ms);
        result.put("p95ms", p95ms);
        result.put("p99ms", p99ms);

        return result;
    }
}
