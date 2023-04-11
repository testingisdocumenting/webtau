/*
 * Copyright 2023 webtau maintainers
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

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.webtau.data.datanode.DataNodeIdParsedPathPart.PartType.CHILD
import static org.testingisdocumenting.webtau.data.datanode.DataNodeIdParsedPathPart.PartType.PEER

class DataNodeIdPathParserTest {
    @Test
    void "empty"() {
        def parsed = DataNodeIdPathParser.parse("")
        parsed.should == []
    }

    @Test
    void "index only"() {
        def parsed = DataNodeIdPathParser.parse("[23]")
        parsed.should == [[idx: 23, childName: "", type: PEER]]
    }

    @Test
    void "index only negative"() {
        def parsed = DataNodeIdPathParser.parse("[-23]")
        parsed.should == [[idx: -23, childName: "", type: PEER]]
    }

    @Test
    void "index wrong content"() {
        code {
            DataNodeIdPathParser.parse("[2-]")
        } should throwException("unexpected char, expected numbers 0-9 (including negative) or closing bracket ], but got: -")
    }

    @Test
    void "single child"() {
        def parsed = DataNodeIdPathParser.parse("object")
        parsed.should == [[idx: null, childName: "object", type: CHILD]]
    }

    @Test
    void "multiple children"() {
        def parsed = DataNodeIdPathParser.parse("object.nested")
        parsed.should == [
                [idx: null, childName: "object", type: CHILD],
                [idx: null, childName: "nested", type: CHILD]]
    }

    @Test
    void "child with index"() {
        def parsed = DataNodeIdPathParser.parse("object[2]")
        parsed.should == [
                [idx: null, childName: "object", type: CHILD],
                [idx: 2, childName: "", type: PEER]]
    }

    @Test
    void "child with index and nested property"() {
        def parsed = DataNodeIdPathParser.parse("object[2].nested")
        parsed.should == [
                [idx: null, childName: "object", type: CHILD],
                [idx: 2, childName: "", type: PEER],
                [idx: null, childName: "nested", type: CHILD]]
    }

    @Test
    void "index and nested property"() {
        def parsed = DataNodeIdPathParser.parse("[2].nested")
        parsed.should == [
                [idx: 2, childName: "", type: PEER],
                [idx: null, childName: "nested", type: CHILD]]
    }

    @Test
    void "child with index and nested with index and nested property"() {
        def parsed = DataNodeIdPathParser.parse("object[2].nested[3].value")
        parsed.should == [
                [idx: null, childName: "object", type: CHILD],
                [idx: 2, childName: "", type: PEER],
                [idx: null, childName: "nested", type: CHILD],
                [idx: 3, childName: "", type: PEER],
                [idx: null, childName: "value", type: CHILD]]
    }
}
