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

package org.testingisdocumenting.webtau.data.datanode

import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.*

class DataNodeListAndTableContainHandlerTest {
    def listOfFullNames = [
            [firstName: 'FN0', lastName: 'LN0'],
            [firstName: 'FN1', lastName: 'LN1'],
            [firstName: 'FN2', lastName: 'LN2'],
            [firstName: 'FN3', lastName: 'LN3']]

    @Test
    void "should check if list contains all entries from table"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        dataNode.should contain(["firstName" | "lastName"] {
                                ___________________________
                                 "FN1"       | "LN1"
                                 "FN2"       | "LN2" })

        dataNode.get(1).get("firstName").traceableValue.checkLevel.should == ExplicitPassed
        dataNode.get(1).get("lastName").traceableValue.checkLevel.should == ExplicitPassed
        dataNode.get(2).get("firstName").traceableValue.checkLevel.should == ExplicitPassed
        dataNode.get(2).get("lastName").traceableValue.checkLevel.should == ExplicitPassed

        dataNode.get(0).get("firstName").traceableValue.checkLevel.should == None
        dataNode.get(0).get("lastName").traceableValue.checkLevel.should == None
        dataNode.get(3).get("firstName").traceableValue.checkLevel.should == None
        dataNode.get(3).get("lastName").traceableValue.checkLevel.should == None
    }

    @Test
    void "one mismatched entry from table"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        TestConsoleOutput.runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting body to contain firstName │ lastName\n' +
                '                                   "FN1_"    │ "LN1"   \n' +
                '                                   "FN2"     │ "LN2"   :\n' +
                '    no matches found for: [{"firstName": "FN1_", "lastName": "LN1"}] (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {"firstName": **"FN0"**, "lastName": **"LN0"**},\n' +
                '    {"firstName": **"FN1"**, "lastName": __"LN1"__},\n' +
                '    {"firstName": **"FN2"**, "lastName": **"LN2"**},\n' +
                '    {"firstName": **"FN3"**, "lastName": **"LN3"**}\n' +
                '  ]') {
            dataNode.should contain(["firstName" | "lastName"] {
                                   ___________________________
                                         "FN1_" | "LN1"
                                         "FN2"  | "LN2" })
        }
    }

    @Test
    void "should not contain entries from table"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        dataNode.shouldNot contain(["firstName" | "lastName"] {
                                    ___________________________
                                    "FN11"      | "LN11"
                                    "FN21"       | "LN21" })
    }

    @Test
    void "should not contain entries from table but contains one"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId('body'), listOfFullNames)

        TestConsoleOutput.runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting body to not contain firstName │ lastName\n' +
                '                                       "FN11"    │ "LN11"  \n' +
                '                                       "FN2"     │ "LN2"   :\n' +
                '    body[2]: equals {"firstName": **"FN2"**, "lastName": **"LN2"**} (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {"firstName": "FN0", "lastName": "LN0"},\n' +
                '    {"firstName": "FN1", "lastName": "LN1"},\n' +
                '    {"firstName": **"FN2"**, "lastName": **"LN2"**},\n' +
                '    {"firstName": "FN3", "lastName": "LN3"}\n' +
                '  ]') {
            dataNode.shouldNot contain(["firstName" | "lastName"] {
                                        ___________________________
                                         "FN11"     | "LN11"
                                         "FN2"      | "LN2" })
        }
    }
}
