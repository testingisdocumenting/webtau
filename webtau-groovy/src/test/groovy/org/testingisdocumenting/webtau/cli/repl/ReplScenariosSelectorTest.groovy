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

package org.testingisdocumenting.webtau.cli.repl

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTest

import java.nio.file.Paths

class ReplScenariosSelectorTest {
    private List<StandaloneTest> availableTests
    private List<ReplScenariosSelector.SelectInputAndResult> results

    @Before
    void init() {
        availableTests = [
                test('my scenario one'),
                test('my scenario two'),
                test('cleanup scenario one'),
                test('cleanup scenario two'),
        ]
    }

    @Test
    void "should select by regexp"() {
        process("two")

        println results.scenario
        results.scenario.should == ['my scenario two']
    }

    @Test
    void "should select by index"() {
        process(2)

        results.scenario.should == ['cleanup scenario one']
    }

    @Test
    void "should have negative index and not found status when is not found"() {
        process(5)

        results.idx.should == [-1]
        results.isFound.should == [false]
        results.scenario.should == [null]
    }

    @Test
    void "should select by index and string combo"() {
        process(1, 'cleanup')

        results.scenario.should == ['my scenario two', 'cleanup scenario one']
    }

    @Test
    void "order of selection should match order of selection"() {
        process('cleanup', 1)

        results.scenario.should == ['cleanup scenario one', 'my scenario two']
    }

    @Test
    void "should select by index range"() {
        process(0:1)
        results.scenario.should == ['my scenario one', 'my scenario two']

        process(0:2)
        results.scenario.should == ['my scenario one', 'my scenario two', 'cleanup scenario one']

        process(2:0)
        results.scenario.should == ['cleanup scenario one', 'my scenario two', 'my scenario one']
    }

    @Test
    void "should select by index range and single entry"() {
        process(0:1, 3)
        results.scenario.should == ['my scenario one', 'my scenario two', 'cleanup scenario two']
    }

    @Test
    void "should select by regexp range"() {
        process('my.*one':'cleanup.*one')
        results.scenario.should == ['my scenario one', 'my scenario two', 'cleanup scenario one']
    }

    @Test
    void "should select by regexp range reversed"() {
        process('cleanup.*one':'my.*one')
        results.scenario.should == ['cleanup scenario one', 'my scenario two', 'my scenario one']
    }

    @Test
    void "should select null when selecting with non existing range"() {
        process('clea2nup.*one':'my2.*one')
        results.scenario.should == [null]
        results.input.should == [['clea2nup.*one':'my2.*one']]
    }

    private void process(Object... selectors) {
        results = new ReplScenariosSelector(availableTests, selectors).inputAndResults
    }

    private static StandaloneTest test(String scenario) {
        return new StandaloneTest(Paths.get(""), Paths.get(""), "cid", scenario, { ->})
    }
}
