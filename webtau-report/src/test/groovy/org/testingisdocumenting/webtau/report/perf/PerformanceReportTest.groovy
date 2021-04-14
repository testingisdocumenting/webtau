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
        def report = new PerformanceReport("myOps")

        101.times {
            report.addOperation("g1", "o${it + 1}", 0, it)
        }

        report.addOperation("g2", "o1", 0, 10)
        report.addOperation("g2", "o2", 0, 20)
        report.addOperation("g2", "o3", 0, 30)
        report.addOperation("g2", "o4", 0, 10)
        report.addOperation("g2", "o5", 0, 20)

        report.calc()

        assert report.aggregatedOperations.size() == 2

        def g1 = report.aggregatedOperations[0]
        g1.should == [
                groupId: "g1",
                count: 101,
                averageMs: 50.0,
                maxMs: 100,
                minMs: 0,
                p20ms: 19.4,
                p50ms: 50.0,
                p80ms: 80.6,
                p95ms: 95.9,
                p99ms: 99.98]

        def g2 = report.aggregatedOperations[1]
        g2.should == [
                groupId: "g2",
                count: 5,
                averageMs: 18.0,
                maxMs: 30,
                minMs: 10,
                p20ms: 10.0,
                p50ms: 20.0,
                p80ms: 28.0,
                p95ms: 30.0,
                p99ms: 30.0]
    }

    @Test
    void "should generate custom report data"() {
        def report = new PerformanceReport("myOps")
        report.addOperation("g1", "o1", 0, 5)
        report.addOperation("g1", "o2", 0, 10)
        report.addOperation("g2", "o1", 0, 10)
        report.addOperation("g2", "o2", 0, 20)

        def reportData = report.build()
        println reportData.toMap()

        reportData.toMap().should == [
                "myOps": ["aggregated": [
                        [
                                groupId: "g1",
                                count: 2,
                                p20ms: 5.0,
                                p50ms: 7.5,
                                p80ms: 10.0,
                                p95ms: 10.0,
                                p99ms: 10.0,
                                minMs: 5,
                                maxMs: 10,
                                averageMs: 7.5
                        ],
                        [
                                groupId: "g2",
                                count: 2,
                                p20ms: 10.0,
                                p50ms: 15.0,
                                p80ms: 20.0,
                                p95ms: 20.0,
                                p99ms: 20.0,
                                minMs: 10,
                                maxMs: 20,
                                averageMs: 15.0
                        ],
                ]]
        ]
    }

    @Test
    void "reset should clear up internal collections"() {
        def report = new PerformanceReport("my ops")
        report.addOperation("g1", "o1", 0, 5)
        report.addOperation("g2", "o2", 0, 20)
        report.calc()

        report.operations.size().shouldNot == 0
        report.aggregatedOperations.size().shouldNot == 0

        report.reset()

        report.operations.size().should == 0
        report.aggregatedOperations.size().should == 0
    }
}
