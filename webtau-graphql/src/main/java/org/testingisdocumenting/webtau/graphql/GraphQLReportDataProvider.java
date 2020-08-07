/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.graphql;

import org.apache.commons.math.stat.descriptive.rank.Percentile;
import org.testingisdocumenting.webtau.report.ReportCustomData;
import org.testingisdocumenting.webtau.report.ReportDataProvider;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphQLReportDataProvider implements ReportDataProvider {
    private final GraphQLCoverage coverage;

    public GraphQLReportDataProvider() {
        this(GraphQL.getCoverage());
    }

    public GraphQLReportDataProvider(GraphQLCoverage coverage) {
        this.coverage = coverage;
    }

    @Override
    public Stream<ReportCustomData> provide(WebTauTestList tests) {
        List<? extends Map<String, ?>> nonCoveredOperations = coverage.nonCoveredOperations()
                .map(GraphQLOperation::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> coveredOperations = coverage.coveredOperations()
                .map(GraphQLOperation::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> timingByOperation = computeTiming();

        Map<String, ?> coverageSummary = computeCoverageSummary();

        return Stream.of(
                new ReportCustomData("graphQLSkippedOperations", nonCoveredOperations),
                new ReportCustomData("graphQLCoveredOperations", coveredOperations),
                new ReportCustomData("graphQLOperationTimeStatistics", timingByOperation),
                new ReportCustomData("graphQLCoverageSummary", coverageSummary));
    }

    private List<? extends Map<String, ?>> computeTiming() {
        return coverage.actualCalls().map(GraphQLReportDataProvider::computeTiming).collect(Collectors.toList());
    }

    private static Map<String, ?> computeTiming(Map.Entry<GraphQLOperation, Set<GraphQLCoveredOperations.Call>> entry) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", entry.getKey().getName());
        data.put("type", entry.getKey().getType().name().toLowerCase());

        Map<String, Object> statistics = new LinkedHashMap<>();
        data.put("statistics", statistics);

        LongSummaryStatistics summaryStatistics = entry.getValue().stream().collect(Collectors.summarizingLong(GraphQLCoveredOperations.Call::getElapsedTime));
        statistics.put("mean", summaryStatistics.getAverage());
        statistics.put("min", summaryStatistics.getMin());
        statistics.put("max", summaryStatistics.getMax());

        double[] times = entry.getValue().stream().map(GraphQLCoveredOperations.Call::getElapsedTime).mapToDouble(Long::doubleValue).sorted().toArray();
        Percentile percentile = new Percentile();
        statistics.put("p95", percentile.evaluate(times, 95));
        statistics.put("p99", percentile.evaluate(times, 99));

        return data;
    }

    private Map<String, ?> computeCoverageSummary() {
        Map<String, Object> summary = new HashMap<>();
        Map<GraphQLOperationType, List<GraphQLOperation>> declaredOpByType = coverage.declaredOperations().collect(Collectors.groupingBy(GraphQLOperation::getType));
        Map<GraphQLOperationType, List<GraphQLOperation>> coveredOpsByType = coverage.coveredOperations().collect(Collectors.groupingBy(GraphQLOperation::getType));

        Map<String, Object> summaryByType = new HashMap<>();
        declaredOpByType.forEach((type, ops) -> {
            double coveredOperations = coveredOpsByType.getOrDefault(type, Collections.emptyList()).size();
            double coverage = coveredOperations / ops.size();

            Map<String, Object> summaryForType = new HashMap<>();
            summaryForType.put("declaredOperations", ops.size());
            summaryForType.put("coveredOperations", coveredOperations);
            summaryForType.put("coverage", coverage);

            summaryByType.put(type.name().toLowerCase(), summaryForType);
        });
        summary.put("types", summaryByType);

        double coveredOperations = coveredOpsByType.values().stream().mapToInt(List::size).sum();
        double declaredOperations = declaredOpByType.values().stream().mapToInt(List::size).sum();
        double totalCoverage = coveredOperations / declaredOperations;

        summary.put("totalDeclaredOperations", declaredOperations);
        summary.put("totalCoveredOperations", coveredOperations);
        summary.put("coverage", totalCoverage);

        return summary;
    }
}
