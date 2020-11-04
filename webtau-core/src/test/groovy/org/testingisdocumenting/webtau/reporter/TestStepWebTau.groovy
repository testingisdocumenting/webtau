/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.reporter

import org.junit.BeforeClass
import org.junit.Test

import java.util.function.Supplier

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action
import static org.testingisdocumenting.webtau.reporter.StepReportOptions.REPORT_ALL
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage
import static java.util.stream.Collectors.toList

class TestStepWebTau {
    static WebTauStep rootStep
    static WebTauStep childStep1
    static WebTauStep childStep2

    @BeforeClass
    static void init() {
        rootStep = createStep("step action") {
            childStep1 = createStep("c1 action")
            childStep1.addPayload(new PayloadA(id: 'id2'))
            childStep1.addPayload(new PayloadB(name: 'name2'))
            childStep1.execute(REPORT_ALL)

            childStep2 = createStep("c2 action")
            childStep2.addPayload(new PayloadA(id: 'id3'))
            childStep2.addPayload(new PayloadB(name: 'name3'))
            childStep2.execute(REPORT_ALL)
        }

        rootStep.addPayload(new PayloadA(id: 'id1'))
        rootStep.addPayload(new PayloadB(name: 'name1'))
        rootStep.execute(REPORT_ALL)
    }

    @Test
    void "create step automatically maintains hierarchy of steps"() {
        def children = rootStep.children().collect(toList())
        assert children.size() == 2

        assert rootStep.numberOfParents == 0
        assert childStep1.numberOfParents == 1
        assert childStep2.numberOfParents == 1
    }

    @Test
    void "step should be able to return a value"() {
        def step = createStep('supplier step', { return 2 + 2 })
        def stepResult = step.execute(REPORT_ALL)

        assert stepResult == 4
    }

    @Test
    void "should recursively return all the payloads from test step and nested test steps"() {
        def payloads = rootStep.getCombinedPayloads().collect(toList())
        assert payloads*.toMap() == [[id: 'id1'], [name: 'name1'], [id: 'id2'], [name: 'name2'], [id: 'id3'], [name: 'name3']]
    }

    @Test
    void "should recursively return payloads of a given type"() {
        def payloadsA = rootStep.getCombinedPayloadsOfType(PayloadA).collect(toList())
        assert payloadsA*.toMap() == [[id: 'id1'], [id: 'id2'], [id: 'id3']]

        def payloadsB = rootStep.getCombinedPayloadsOfType(PayloadB).collect(toList())
        assert payloadsB*.toMap() == [[name: 'name1'], [name: 'name2'], [name: 'name3']]
    }

    @Test
    void "should know if a payload of a certain type is present"() {
        assert rootStep.hasPayload(PayloadA)
        assert rootStep.hasPayload(PayloadB)
        assert ! rootStep.hasPayload(PayloadC)
    }

    @Test
    void "should count number of failed and successful steps"() {
        def root = createStep("root step action") {
            def step1 = createStep("c1 action") {
                throw new RuntimeException("error")
            }

            try {
                step1.execute(REPORT_ALL)
            } catch (Exception ignored) {
            }

            def step2 = createStep("c2 action")
            step2.execute(REPORT_ALL)

            def step3 = createStep("c3 action") {
                def step31 = createStep("c31 action", { throw new RuntimeException("error2") })
                step31.execute(REPORT_ALL)

                def step32 = createStep("c32 action")
                step32.execute(REPORT_ALL)
            }

            try {
                step3.execute(REPORT_ALL)
            } catch (Exception ignored) {
            }
        }

        root.execute(REPORT_ALL)

        assert root.calcNumberOfFailedSteps() == 3
        assert root.calcNumberOfSuccessfulSteps() == 2
    }

    private static WebTauStep createStep(String title, Supplier stepCode = { return null }) {
        return WebTauStep.createStep(null, tokenizedMessage(action(title)), {
            tokenizedMessage(action('done ' + title))
        } as Supplier, stepCode)
    }

    private static class PayloadA implements WebTauStepPayload {
        String id

        @Override
        Map<String, ?> toMap() {
            return [id: id]
        }
    }

    private static class PayloadB implements WebTauStepPayload {
        String name

        @Override
        Map<String, ?> toMap() {
            return [name: name]
        }
    }

    private static class PayloadC implements WebTauStepPayload {
        @Override
        Map<String, ?> toMap() {
            return [:]
        }
    }
}
