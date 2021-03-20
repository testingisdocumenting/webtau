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

package org.testingisdocumenting.webtau.console

import org.junit.Before
import org.junit.Test

class IndentedConsoleOutputTest {
    TestConsoleOutput console

    @Before
    void init() {
        console = new TestConsoleOutput()
    }

    @Test
    void "should indent output"() {
        def indented =new IndentedConsoleOutput(console, 4)
        indented.out('hello', 200L, 'world')

        assert console.out == [['    ', 'hello', 200L, 'world']]
        assert console.err == []
    }

    @Test
    void "should indent error"() {
        def indented =new IndentedConsoleOutput(console, 4)
        indented.err('hello', 200L, 'world')

        assert console.err == [['    ', 'hello', 200L, 'world']]
        assert console.out == []
    }

    private static class TestConsoleOutput implements ConsoleOutput {
        private List<List<Object>> out = new ArrayList<>()
        private List<List<Object>> err = new ArrayList<>()


        @Override
        void out(Object... styleOrValues) {
            out.push(Arrays.asList(styleOrValues))
        }

        @Override
        void err(Object... styleOrValues) {
            err.push(Arrays.asList(styleOrValues))
        }
    }
}
