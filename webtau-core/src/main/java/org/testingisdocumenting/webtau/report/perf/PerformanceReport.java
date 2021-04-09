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

import org.apache.commons.math.stat.descriptive.rank.Percentile;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

public class PerformanceReport {
    private String label;
    private final List<OperationPerformance> operations;
    private final List<OperationAggregatedPerformance> aggregatedOperations;

    public PerformanceReport(String label) {
        this.label = label;
        this.operations = new ArrayList<>();
        this.aggregatedOperations = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addOperation(OperationPerformance operation) {
        this.operations.add(operation);
    }

    public List<OperationAggregatedPerformance> getAggregatedOperations() {
        return aggregatedOperations;
    }

    public void aggregate() {
        Map<String, List<OperationPerformance>> byGroupId = operations.stream()
                .collect(Collectors.groupingBy(OperationPerformance::getGroupId));

        aggregatedOperations.clear();
        aggregatedOperations.addAll(byGroupId.values().stream()
            .map(this::aggregateSingleGroup)
                .collect(Collectors.toList()));
    }

    private OperationAggregatedPerformance aggregateSingleGroup(List<OperationPerformance> operations) {
        OperationAggregatedPerformance result = new OperationAggregatedPerformance();
        result.setGroupId(operations.get(0).getGroupId());

        LongSummaryStatistics summaryStatistics = operations
                .stream()
                .collect(Collectors.summarizingLong(OperationPerformance::getElapsedMs));

        result.setAverageMs(summaryStatistics.getAverage());
        result.setMinMs(summaryStatistics.getMin());
        result.setMaxMs(summaryStatistics.getMax());
        result.setCount(summaryStatistics.getCount());

        double[] times = operations.stream()
                .map(OperationPerformance::getElapsedMs)
                .mapToDouble(Long::doubleValue)
                .sorted()
                .toArray();

        Percentile percentile = new Percentile();
        result.setP50ms(percentile.evaluate(times, 50));
        result.setP20ms(percentile.evaluate(times, 20));
        result.setP80ms(percentile.evaluate(times, 80));
        result.setP95ms(percentile.evaluate(times, 95));
        result.setP99ms(percentile.evaluate(times, 99));

        return result;
    }
}
