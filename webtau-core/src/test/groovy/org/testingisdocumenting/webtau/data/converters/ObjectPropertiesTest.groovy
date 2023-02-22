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

package org.testingisdocumenting.webtau.data.converters

import org.junit.Test
import org.testingisdocumenting.webtau.data.Account
import org.testingisdocumenting.webtau.data.GameConfig
import org.testingisdocumenting.webtau.expectation.equality.handlers.SmallBean

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runCaptureAndValidateOutput

class ObjectPropertiesTest {
    @Test
    void "trace object properties"() {
        def object = new SmallBean()
        runAndValidateOutput('[tracing] my object\n' +
                '  {"description": "d1", "name": "n1", "price": 100}') {
            trace("my object", properties(object))
        }
    }

    @Test
    void "trace list of objects properties"() {
        def a = new SmallBean()
        def b = new SmallBean(2)

        runAndValidateOutput('[tracing] my object\n' +
                '  [\n' +
                '    {"description": "d1", "name": "n1", "price": 100},\n' +
                '    {"description": "d2", "name": "n2", "price": 200}\n' +
                '  ]') {
            trace("my object", properties([a, b]))

        }
    }

    @Test
    void "trace table of objects properties"() {
        def a = new SmallBean()
        def b = new SmallBean(2)

        runAndValidateOutput('[tracing] my object\n' +
                '  description │ name │ price\n' +
                '  "d1"        │ "n1" │   100\n' +
                '  "d2"        │ "n2" │   200') {
            trace("my object", propertiesTable([a, b]))
        }
    }

    @Test
    void "nested simple bean"() {
        // data-prep
        def account = new Account("acc1", "my account", 100)
        // data-prep

        runCaptureAndValidateOutput('nested-simple-bean-trace-output', '[tracing] my account\n' +
                '  {"id": "acc1", "money": {"dollars": 100}, "name": "my account"}') {
            // data-trace
            trace("my account", properties(account))
            // data-trace
        }
    }

    @Test
    void "table of complex beans"() {
        // data-prep
        def game1 = new GameConfig("super game", Paths.get("/games/superA"))
        game1.registerAchievement("chapter1", "Chapter One", "complete chapter one")
        game1.registerAchievement("boss1", "Giant Rat", "defeat giant rat")

        def game2 = new GameConfig("duper game", Paths.get("/games/duperA"))
        game2.registerAchievement("chapter2", "Chapter Two", "complete chapter two")
        game2.registerAchievement("boss2", "Giant Mouse", "defeat giant mouse")
        // data-prep

        runCaptureAndValidateOutput('table-properties-trace-output', '[tracing] games\n' +
                '  achievements                                                                        │ gameName     │ location     \n' +
                '  [                                                                                   │ "super game" │ /games/superA\n' +
                '    {"description": "complete chapter one", "id": "chapter1", "name": "Chapter One"}, │              │              \n' +
                '    {"description": "defeat giant rat", "id": "boss1", "name": "Giant Rat"}           │              │              \n' +
                '  ]                                                                                   │              │              \n' +
                '                                                                                      │              │              \n' +
                '  [                                                                                   │ "duper game" │ /games/duperA\n' +
                '    {"description": "complete chapter two", "id": "chapter2", "name": "Chapter Two"}, │              │              \n' +
                '    {"description": "defeat giant mouse", "id": "boss2", "name": "Giant Mouse"}       │              │              \n' +
                '  ]                                                                                   │              │              ') {
            // data-trace
            trace("games", propertiesTable([game1, game2]))
            // data-trace
        }
    }

    @Test
    void "table of objects with nulls"() {
        def game1 = new GameConfig(null, Paths.get("/games/superA"))
        def game2 = new GameConfig("duper game", null)

        game1.registerAchievement("id1", "name", null)

        runAndValidateOutput('[tracing] games\n' +
                '  achievements                                         │ gameName     │ location     \n' +
                '  [{"description": null, "id": "id1", "name": "name"}] │ null         │ /games/superA\n' +
                '  []                                                   │ "duper game" │ null         ') {
            trace("games", propertiesTable([game1, game2]))
        }
    }
}
