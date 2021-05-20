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
import org.testingisdocumenting.webtau.reporter.WebTauReportCustomData;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

public class PerformanceReport {
    private final String id;
    private final List<OperationPerformance> operations;
    private final List<OperationAggregatedPerformance> aggregatedOperations;
    private final OperationsPerformanceHistogram performanceHistogram;
    private OperationAggregatedPerformance overallSummary;

    public PerformanceReport(String id) {
        this.id = id;
        this.operations = new ArrayList<>();
        this.aggregatedOperations = new ArrayList<>();
        this.performanceHistogram = new OperationsPerformanceHistogram(50);
    }

    public synchronized void reset() {
        operations.clear();
        aggregatedOperations.clear();
    }

    public String getId() {
        return id;
    }

    public synchronized void addOperation(String uniqueId,
                                          String groupId,
                                          String operationId,
                                          long startTime,
                                          long elapsedMs) {
        OperationPerformance operation = new OperationPerformance(uniqueId, groupId, operationId, startTime, elapsedMs);
        operations.add(operation);
        performanceHistogram.addOperation(operation);
    }

    public List<OperationPerformance> getOperations() {
        return operations;
    }

    public List<OperationAggregatedPerformance> getAggregatedOperations() {
        return aggregatedOperations;
    }

    public WebTauReportCustomData build() {
        calc();

        List<Map<String, Object>> aggregated = aggregatedOperations.stream()
                .map(OperationAggregatedPerformance::toMap)
                .collect(Collectors.toList());

        return new WebTauReportCustomData(id, CollectionUtils.aMapOf(
                "aggregated", aggregated,
                "operationsById", operations.stream()
                        .collect(Collectors.toMap(OperationPerformance::getUniqueId, OperationPerformance::toMap)),
                "histogram", performanceHistogram.toMap(),
                "summary", overallSummary.toMap()
        ));
    }

    synchronized void calc() {
        Map<String, List<OperationPerformance>> byGroupId = operations.stream()
                .collect(Collectors.groupingBy(OperationPerformance::getGroupId));

        overallSummary = aggregateGroup("allOperations", operations);

        aggregatedOperations.clear();
        aggregatedOperations.addAll(
                byGroupId.values().stream()
                        .map(operations -> aggregateGroup(operations.get(0).getGroupId(), operations))
                        .collect(Collectors.toList()));
    }

    private OperationAggregatedPerformance aggregateGroup(String groupIdToUse, List<OperationPerformance> operations) {
        OperationAggregatedPerformance result = new OperationAggregatedPerformance();
        result.setGroupId(groupIdToUse);

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
