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

package org.testingisdocumenting.webtau.report.perf

import org.junit.Test

class PerformanceReportTest {
    @Test
    void "should aggregate operations by id and calculate their perf stats"() {
        def report = new PerformanceReport("my ops")

        101.times {
            report.addOperation(new OperationPerformance("g1", "o${it + 1}", 0, it))
        }

        report.addOperation(new OperationPerformance("g2", "o1", 0, 10))
        report.addOperation(new OperationPerformance("g2", "o2", 0, 20))
        report.addOperation(new OperationPerformance("g2", "o3", 0, 30))
        report.addOperation(new OperationPerformance("g2", "o4", 0, 10))
        report.addOperation(new OperationPerformance("g2", "o5", 0, 20))

        report.aggregate()

        assert report.aggregatedOperations.size() == 2

        def g1 = report.aggregatedOperations[0]
        assert g1.groupId == "g1"
        assert g1.count == 101
        assert g1.averageMs == 50.0d
        assert g1.maxMs == 100
        assert g1.minMs == 0
        assert g1.p20ms == 19.4d
        assert g1.p50ms == 50.0d
        assert g1.p80ms == 80.6d
        assert g1.p95ms == 95.9d
        assert g1.p99ms == 99.98d

        def g2 = report.aggregatedOperations[1]
        assert g2.groupId == "g2"
        assert g2.count == 5
        assert g2.averageMs == 18.0d
        assert g2.maxMs == 30
        assert g2.minMs == 10
        assert g2.p20ms == 10.0d
        assert g2.p50ms == 20.0d
        assert g2.p80ms == 28.0d
        assert g2.p95ms == 30.0d
        assert g2.p99ms == 30.0d
    }
}
