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

package org.testingisdocumenting.webtau.data

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.data.table.TableData
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep
import org.testingisdocumenting.webtau.utils.JsonParseException

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.webtau.data.Data.data

class DataJsonTest implements StepReporter {
    def failedSteps = []

    @Before
    void init() {
        StepReporters.add(this)
        StepReporters.add(StepReporters.defaultStepReporter)
        failedSteps.clear()
    }

    @After
    void cleanup() {
        StepReporters.remove(this)
        StepReporters.remove(StepReporters.defaultStepReporter)
    }

    @Test
    void "parse json as map from resource"() {
        Map map = data.json.map("map.json")
        map.should == [key: 'value', another: 2]
    }

    @Test
    void "parse json as map from path as string"() {
        Map map = data.json.map("src/test/resources/map.json")
        map.should == [key: 'value', another: 2]
    }

    @Test
    void "parse json as map from path as Path"() {
        Map map = data.json.map(Paths.get("src/test/resources/map.json"))
        map.should == [key: 'value', another: 2]
    }

    @Test
    void "parse json as object from resource"() {
        def object = data.json.object("data/single.json")
        object.should == "hello world"
    }

    @Test
    void "parse json as object from string"() {
        def object = data.json.objectFromString("\"hello world\"")
        object.should == "hello world"
    }

    @Test
    void "parse map from string"() {
        def map = data.json.mapFromString("""{"key": "value"}""")
        map.should == [key: "value"]
    }

    @Test
    void "parse json as list"() {
        List list = data.json.list("list.json")
        list.should == [
                [key: 'value1', another: 1],
                [key: 'value2', another: 2]]
    }

    @Test
    void "parse list from string"() {
        def list = data.json.listFromString("""[{"key1": "value1"}, {"key2": "value2"}]""")
        list.should == [
                [key1: "value1"],
                [key2: "value2"]]
    }

    @Test
    void "parse json as object"() {
        def object = data.json.object("map.json")
        object.should == [key: 'value', another: 2]
    }

    @Test
    void "parse json as table"() {
        TableData table = data.json.table("list.json")
        table.should == ["key"    | "another"] {
                        ________________________
                         "value1" |     1
                         "value2" |     2 }
    }

    @Test
    void "parse table from string"() {
       def table = data.json.tableFromString("""
        [
          {
            "id": "id1",
            "name": "hello",
            "payload": {
              "info": "additional id1 payload"
            }
          },
          {
            "id": "id2",
            "name": "world",
            "payload": {
              "info": "additional id2 payload"
            }
          }
        ]
        """)

        table.should == [ "id" |  "name" |           "payload"] {
                         _________________________________________
                         "id1" | "hello" | [info: ~/id1 payload/]
                         "id2" | "world" | [info: ~/id2 payload/] }
    }

    @Test
    void "parse json as table from map should fail"() {
        code {
            data.json.table("map.json")
        } should throwException("only JSON list of objects can be converted to TableData")

        code {
            data.json.table("list-of-primitives.json")
        } should throwException("only JSON list of objects can be converted to TableData")

        code {
            data.json.table("list-of-different-types.json")
        } should throwException("only JSON list of objects can be converted to TableData")
    }

    @Test
    void "parse json error should capture as failed step"() {
        code {
            def object = data.json.object("broken.json")
            object.should == [key: 'value', another: 2]
        } should throwException(JsonParseException)

        failedSteps[0].completionMessage.toString().should contain("failed reading json from file or resource broken.json : Unexpected character")
    }

    @Test
    void "non existing resource should be reported"() {
        code {
            data.json.map("map.ajson")
        } should throwException(~/Can't find resource "map\.ajson" or file ".*webtau-data\/map\.ajson"/)
    }

    @Override
    void onStepStart(WebTauStep step) {

    }

    @Override
    void onStepSuccess(WebTauStep step) {

    }

    @Override
    void onStepFailure(WebTauStep step) {
        failedSteps.add(step)
    }
}
