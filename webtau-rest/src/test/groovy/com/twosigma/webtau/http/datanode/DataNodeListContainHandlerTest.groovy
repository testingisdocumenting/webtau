/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http.datanode

import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.Ddjt.throwException
import static com.twosigma.webtau.data.traceable.CheckLevel.*

class DataNodeListContainHandlerTest {
    def listOfNames = [
        [firstName: 'FN0', lastName: 'LN0'],
        [firstName: 'FN1', lastName: 'LN1'],
        [firstName: 'FN2', lastName: 'LN2'],
        [firstName: 'FN3', lastName: 'LN3']]

    @Test
    void "should mark containing items with passed status and not change other item statuses"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("body"), listOfNames)

        dataNode.get(0).get('firstName').get().updateCheckLevel(FuzzyPassed)

        dataNode.should contain([firstName: 'FN3', lastName: 'LN3'])

        dataNode.get(0).get('firstName').get().checkLevel.should == FuzzyPassed
        dataNode.get(0).get('lastName').get().checkLevel.should == None
        dataNode.get(1).get('firstName').get().checkLevel.should == None
        dataNode.get(1).get('lastName').get().checkLevel.should == None
        dataNode.get(2).get('firstName').get().checkLevel.should == None
        dataNode.get(2).get('lastName').get().checkLevel.should == None
        dataNode.get(3).get('firstName').get().checkLevel.should == ExplicitPassed
        dataNode.get(3).get('lastName').get().checkLevel.should == ExplicitPassed
    }

    @Test
    void "should mark all items as failed when item is not present"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("body"), listOfNames)

        code {
            dataNode.should contain([firstName: 'FN8', lastName: 'LN8'])
        } should throwException(~/body expect to contain \{firstName=FN8, lastName=LN8}/)

        dataNode.elements().collect { it.get('firstName').get().checkLevel }.should == [ExplicitFailed, ExplicitFailed, ExplicitFailed, ExplicitFailed]
        dataNode.elements().collect { it.get('lastName').get().checkLevel }.should == [ExplicitFailed, ExplicitFailed, ExplicitFailed, ExplicitFailed]
    }

    @Test
    void "should mark all items as fuzzy passed when item is not present and should not be present"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("body"), [1, 2, 3])
        dataNode.shouldNot contain(8)

        dataNode.elements().collect { it.get().checkLevel }.should == [FuzzyPassed, FuzzyPassed, FuzzyPassed]
    }

    @Test
    void "should mark containing items as failed when when they should not be present"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("body"), listOfNames)

        code {
            dataNode.shouldNot contain([firstName: 'FN2', lastName: 'LN2'])
        } should throwException(~/body expect to not contain \{firstName=FN2, lastName=LN2}/)

        dataNode.get(0).get('firstName').get().checkLevel.should == None
        dataNode.get(0).get('lastName').get().checkLevel.should == None
        dataNode.get(1).get('firstName').get().checkLevel.should == None
        dataNode.get(1).get('lastName').get().checkLevel.should == None
        dataNode.get(2).get('firstName').get().checkLevel.should == ExplicitFailed
        dataNode.get(2).get('lastName').get().checkLevel.should == ExplicitFailed
        dataNode.get(3).get('firstName').get().checkLevel.should == None
        dataNode.get(3).get('lastName').get().checkLevel.should == None
    }
}
