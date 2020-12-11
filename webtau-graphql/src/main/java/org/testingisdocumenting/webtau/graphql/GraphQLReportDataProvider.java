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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphQLReportDataProvider implements ReportDataProvider {
    private final Supplier<GraphQLCoverage> coverageSupplier;

    public GraphQLReportDataProvider() {
        this(GraphQL::getCoverage);
    }

    public GraphQLReportDataProvider(Supplier<GraphQLCoverage> coverageSupplier) {
        this.coverageSupplier = coverageSupplier;
    }

    @Override
    public Stream<ReportCustomData> provide(WebTauTestList tests) {
        List<? extends Map<String, ?>> nonCoveredQueries = coverageSupplier.get().nonCoveredQueries()
                .map(GraphQLQuery::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> coveredQueries = coverageSupplier.get().coveredQueries()
                .map(GraphQLQuery::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> successBranches = coverageSupplier.get().coveredSuccessBranches()
                .map(GraphQLQuery::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> nonCoveredSuccessBranches = coverageSupplier.get().nonCoveredSuccessBranches()
                .map(GraphQLQuery::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> errorBranches = coverageSupplier.get().coveredErrorBranches()
                .map(GraphQLQuery::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> nonCoveredErrorBranches = coverageSupplier.get().nonCoveredErrorBranches()
                .map(GraphQLQuery::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> timingByQuery = computeTiming();

        Map<String, ?> coverageSummary = computeCoverageSummary();

    return Stream.of(
        new ReportCustomData("graphQLSkippedQueries", nonCoveredQueries),
        new ReportCustomData("graphQLCoveredQueries", coveredQueries),
        new ReportCustomData("graphQLSkippedSuccessBranches", nonCoveredSuccessBranches),
        new ReportCustomData("graphQLCoveredSuccessBranches", successBranches),
        new ReportCustomData("graphQLSkippedErrorBranches", nonCoveredErrorBranches),
        new ReportCustomData("graphQLCoveredErrorBranches", errorBranches),
        new ReportCustomData("graphQLQueryTimeStatistics", timingByQuery),
        new ReportCustomData("graphQLCoverageSummary", coverageSummary));
    }

    private List<? extends Map<String, ?>> computeTiming() {
        return coverageSupplier.get().actualCalls().map(GraphQLReportDataProvider::computeTiming).collect(Collectors.toList());
    }

    private static Map<String, ?> computeTiming(Map.Entry<GraphQLQuery, Set<GraphQLCoveredQueries.Call>> entry) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", entry.getKey().getName());
        data.put("type", entry.getKey().getType().name().toLowerCase());

        Map<String, Object> statistics = new LinkedHashMap<>();
        data.put("statistics", statistics);

        LongSummaryStatistics summaryStatistics = entry.getValue().stream().collect(Collectors.summarizingLong(GraphQLCoveredQueries.Call::getElapsedTime));
        statistics.put("mean", summaryStatistics.getAverage());
        statistics.put("min", summaryStatistics.getMin());
        statistics.put("max", summaryStatistics.getMax());
        statistics.put("count", summaryStatistics.getCount());

        double[] times = entry.getValue().stream().map(GraphQLCoveredQueries.Call::getElapsedTime).mapToDouble(Long::doubleValue).sorted().toArray();
        Percentile percentile = new Percentile();
        statistics.put("p95", percentile.evaluate(times, 95));
        statistics.put("p99", percentile.evaluate(times, 99));

        return data;
    }

    private Map<String, ?> computeCoverageSummary() {
        Map<String, Object> summary = new HashMap<>();
        Map<GraphQLQueryType, List<GraphQLQuery>> declaredQueriesByType = coverageSupplier.get().declaredQueries().collect(Collectors.groupingBy(GraphQLQuery::getType));
        Map<GraphQLQueryType, List<GraphQLQuery>> coveredQueriesByType = coverageSupplier.get().coveredQueries().collect(Collectors.groupingBy(GraphQLQuery::getType));

        Map<String, Object> summaryByType = new HashMap<>();
        declaredQueriesByType.forEach((type, queries) -> {
            double coveredQueries = coveredQueriesByType.getOrDefault(type, Collections.emptyList()).size();
            double coverage = coveredQueries / queries.size();

            Map<String, Object> summaryForType = new HashMap<>();
            summaryForType.put("declaredQueries", queries.size());
            summaryForType.put("coveredQueries", coveredQueries);
            summaryForType.put("coverage", coverage);

            summaryByType.put(type.name().toLowerCase(), summaryForType);
        });
        summary.put("types", summaryByType);

        double coveredQueries = coveredQueriesByType.values().stream().mapToInt(List::size).sum();
        double successBranches = coverageSupplier.get().coveredSuccessBranches().count();
        double errorBranches = coverageSupplier.get().coveredErrorBranches().count();
        double declaredQueries = declaredQueriesByType.values().stream().mapToInt(List::size).sum();
        double totalCoverage = coveredQueries / declaredQueries;
        double successBranchCoverage = successBranches / declaredQueries;
        double errorBranchCoverage = errorBranches / declaredQueries;
        double overallBranchCoverage = computeOverallBranchCoverage(successBranchCoverage,
            errorBranchCoverage);

        summary.put("totalDeclaredQueries", declaredQueries);
        summary.put("totalCoveredQueries", coveredQueries);
        summary.put("coverage", totalCoverage);
        summary.put("successBranchCoverage", successBranchCoverage);
        summary.put("errorBranchCoverage", errorBranchCoverage);
        summary.put("branchCoverage", overallBranchCoverage);

        return summary;
    }

    /**
     * full branch coverage = success + error coverage for each declared query
     *                      = (successBranches + errorBranches) / 2 * declaredQueries
     *                      = (successBranchCoverage + errorBranchCoverage) / 2
     */
    private double computeOverallBranchCoverage(double successBranchCoverage,
        double errorBranchCoverage) {
        return (successBranchCoverage + errorBranchCoverage) / 2;
    }
}
