/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datanode

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.ExplicitFailed
import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.ExplicitPassed
import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.FuzzyPassed
import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.None
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput

class DataNodeListContainHandlerTest {
    def listOfFirstNames = ['FN0', 'FN1', 'FN2', 'FN3']

    def listOfFullNames = [
        [firstName: 'FN0', lastName: 'LN0'],
        [firstName: 'FN1', lastName: 'LN1'],
        [firstName: 'FN2', lastName: 'LN2'],
        [firstName: 'FN3', lastName: 'LN3']]


    @Test
    void "should mark single containing items with passed status and not change other item statuses"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFirstNames)

        dataNode.get(0).getTraceableValue().updateCheckLevel(FuzzyPassed)

        dataNode.should contain('FN2')

        dataNode.get(0).getTraceableValue().checkLevel.should == FuzzyPassed
        dataNode.get(1).getTraceableValue().checkLevel.should == None
        dataNode.get(2).getTraceableValue().checkLevel.should == ExplicitPassed
        dataNode.get(3).getTraceableValue().checkLevel.should == None
    }

    @Test
    void "should mark containing items with passed status and not change other item statuses"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        dataNode.get(0).get('firstName').getTraceableValue().updateCheckLevel(FuzzyPassed)

        dataNode.should contain([firstName: 'FN3', lastName: 'LN3'])

        dataNode.get(0).get('firstName').getTraceableValue().checkLevel.should == FuzzyPassed
        dataNode.get(0).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(1).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(1).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(2).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(2).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(3).get('firstName').getTraceableValue().checkLevel.should == ExplicitPassed
        dataNode.get(3).get('lastName').getTraceableValue().checkLevel.should == ExplicitPassed
    }

    @Test
    void "should mark containing items with passed status and not change other item statuses when compare derived items"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        dataNode.get(0).get('firstName').getTraceableValue().updateCheckLevel(FuzzyPassed)

        dataNode.get("firstName").should contain('FN3')

        dataNode.get(0).get('firstName').getTraceableValue().checkLevel.should == FuzzyPassed
        dataNode.get(0).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(1).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(1).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(2).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(2).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(3).get('firstName').getTraceableValue().checkLevel.should == ExplicitPassed
        dataNode.get(3).get('lastName').getTraceableValue().checkLevel.should == None
    }

    @Test
    void "should mark all items as failed when item is not present"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        runAndValidateOutput(~/body expects to contain \{firstName=FN8, lastName=LN8}/) {
            dataNode.should contain([firstName: 'FN8', lastName: 'LN8'])
        }

        dataNode.elements().collect { it.get('firstName').getTraceableValue().checkLevel }.should == [ExplicitFailed, ExplicitFailed, ExplicitFailed, ExplicitFailed]
        dataNode.elements().collect { it.get('lastName').getTraceableValue().checkLevel }.should == [ExplicitFailed, ExplicitFailed, ExplicitFailed, ExplicitFailed]
    }

    @Test
    void "should mark all items as fuzzy passed when item is not present and should not be present"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), [1, 2, 3])
        dataNode.shouldNot contain(8)

        dataNode.elements().collect { it.getTraceableValue().checkLevel }.should == [FuzzyPassed, FuzzyPassed, FuzzyPassed]
    }

    @Test
    void "should mark containing items as failed when when they should not be present"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        runAndValidateOutput(~/body expects to not contain \{firstName=FN2, lastName=LN2}/) {
            dataNode.shouldNot contain([firstName: 'FN2', lastName: 'LN2'])
        }

        dataNode.get(0).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(0).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(1).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(1).get('lastName').getTraceableValue().checkLevel.should == None
        dataNode.get(2).get('firstName').getTraceableValue().checkLevel.should == ExplicitFailed
        dataNode.get(2).get('lastName').getTraceableValue().checkLevel.should == ExplicitFailed
        dataNode.get(3).get('firstName').getTraceableValue().checkLevel.should == None
        dataNode.get(3).get('lastName').getTraceableValue().checkLevel.should == None
    }
}
