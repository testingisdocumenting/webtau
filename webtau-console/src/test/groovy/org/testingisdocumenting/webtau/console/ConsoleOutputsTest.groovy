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

package org.testingisdocumenting.webtau.console

import org.junit.After
import org.junit.Before
import org.junit.Test

class ConsoleOutputsTest implements ConsoleOutput {
    List<Object> output = []

    @Before
    void init() {
        ConsoleOutputs.add(this)
    }

    @After
    void cleanup() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "should print lines with limit and ellipsis"() {
        assert outLinesWithLimit(['hello', 'world', 'of', 'limit'], 1000) == ['hello', 'world', 'of', 'limit']
        assert outLinesWithLimit(['hello', 'world', 'of', 'limit'], 4) == ['hello', 'world', 'of', 'limit']
        assert outLinesWithLimit(['hello', 'world', 'of', 'limit'], 3) == ['hello', '...', 'of', 'limit']
        assert outLinesWithLimit(['hello', 'world', 'of', 'limit'], 2) == ['hello', '...', 'limit']
        assert outLinesWithLimit(['hello', 'world', 'of', 'limit'], 1) == ['...', 'limit']
        assert outLinesWithLimit(['hello', 'world', 'of', 'limit'], 0) == ['...']
    }

    @Override
    void out(Object... styleOrValues) {
        styleOrValues
                .findAll { it instanceof String }
                .each { output << it }
    }

    @Override
    void err(Object... styleOrValues) {
    }

    private List<Object> outLinesWithLimit(List<String> lines, int limit) {
        output = []
        ConsoleOutputs.outLinesWithLimit(lines, limit) { [it] as Object[] }

        return output
    }
}
