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

class OperationsPerformanceHistogramTest {
    @Test
    void "should calc bucket minMs by step and elapsed time"() {
        def histogram50 = new OperationsPerformanceHistogram(50)
        histogram50.calcBucketMinMs(0).should == 0
        histogram50.calcBucketMinMs(10).should == 0
        histogram50.calcBucketMinMs(49).should == 0
        histogram50.calcBucketMinMs(50).should == 50
        histogram50.calcBucketMinMs(52).should == 50
        histogram50.calcBucketMinMs(99).should == 50
        histogram50.calcBucketMinMs(120).should == 100
    }

    @Test
    void "should assign operations to buckets based on latency"() {
        def histogram100 = new OperationsPerformanceHistogram(100)

        def o1 = new OperationPerformance("uid1", "g1", "o1", 0, 10)
        def o2 = new OperationPerformance("uid2", "g1", "o2", 20, 110)
        def o3 = new OperationPerformance("uid3", "g1", "o3", 30, 123)
        def o4 = new OperationPerformance("uid4", "g1", "o4", 40, 200)
        def o5 = new OperationPerformance("uid5", "g1", "o5", 50, 220)

        histogram100.addOperation(o2)
        histogram100.addOperation(o1)
        histogram100.addOperation(o5)
        histogram100.addOperation(o3)
        histogram100.addOperation(o4)

        histogram100.toMap().should == [stepMs: 100, buckets: [
                [minMsInclusive: 0, maxMsExclusive: 100, operationUniqueIds: [o1.uniqueId]],
                [minMsInclusive: 100, maxMsExclusive: 200, operationUniqueIds: [o2.uniqueId, o3.uniqueId]],
                [minMsInclusive: 200, maxMsExclusive: 300, operationUniqueIds: [o5.uniqueId, o4.uniqueId]]]]
    }
}
