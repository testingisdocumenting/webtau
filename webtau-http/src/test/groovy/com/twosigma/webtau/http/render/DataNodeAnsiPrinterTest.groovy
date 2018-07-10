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

package com.twosigma.webtau.http.render

import com.twosigma.webtau.console.ConsoleOutput
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.console.ansi.FontStyle
import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.Ddjt.equal

class DataNodeAnsiPrinterTest {
    private static def ansiConsoleOutput = new AnsiConsoleOutput()

    @BeforeClass
    static void init() {
        ConsoleOutputs.add(ansiConsoleOutput)
    }

    @AfterClass
    static void cleanup() {
        ConsoleOutputs.remove(ansiConsoleOutput)
    }

    @Test
    void "should print list data node with indentation and using different colors"() {
        def textOnly = withCapturedOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromList(new DataNodeId("root"), [1, 2, 3, 4]))
        }

        Assert.assertEquals('[\n' +
                '  1,\n' +
                '  2,\n' +
                '  3,\n' +
                '  4\n' +
                ']', textOnly)
    }

    @Test
    void "should print object data node"() {
        def textOnly = withCapturedOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                    key1: 'value1',
                    key2: 'value2',
                    key3: [key31: 'value31', key32: [5, 6, 8]],
                    key4: [key41: 'value41', key42: 'value42']]))
        }

        Assert.assertEquals('{\n' +
                '  "key1": "value1",\n' +
                '  "key2": "value2",\n' +
                '  "key3": {\n' +
                '    "key31": "value31",\n' +
                '    "key32": [\n' +
                '      5,\n' +
                '      6,\n' +
                '      8\n' +
                '    ]\n' +
                '  },\n' +
                '  "key4": {\n' +
                '    "key41": "value41",\n' +
                '    "key42": "value42"\n' +
                '  }\n' +
                '}', textOnly)
    }

    @Test
    void "should print list of objects outlining checklevel"() {
        def o1 = [key1: 'value1',
                  key2: 'value2',
                  key3: [key31: 'value31', key32: [5, 6, 8]]]

        def o2 = [key4: 'value4',
                 key5: 'value5']

        def dataNode = DataNodeBuilder.fromList(new DataNodeId("root"), [o1, o2])

        dataNode.get(0).get('key2').should(equal('value2'))

        try {
            dataNode.get(0).get('key3').get('key31') should(equal('wrong-value'))
        } catch (AssertionError e) {
            // catch as it is an expected here and serves to check if the value will be rendered as failed
        }

        def textOnly = withCapturedOutput {
            new DataNodeAnsiPrinter().print(dataNode)
        }

        Assert.assertEquals('[\n' +
                '  {\n' +
                '    "key1": "value1",\n' +
                '    "key2": __"value2"__,\n' +
                '    "key3": {\n' +
                '      "key31": **"value31"**,\n' +
                '      "key32": [\n' +
                '        5,\n' +
                '        6,\n' +
                '        8\n' +
                '      ]\n' +
                '    }\n' +
                '  },\n' +
                '  {\n' +
                '    "key4": "value4",\n' +
                '    "key5": "value5"\n' +
                '  }\n' +
                ']', textOnly)
    }

    @Test
    void "should collapse empty list and object"() {
        def textOnly = withCapturedOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                    key1: 'value1',
                    key2: 'value2',
                    key3: [],
                    key4: [:]]))
        }

        Assert.assertEquals('{\n' +
                '  "key1": "value1",\n' +
                '  "key2": "value2",\n' +
                '  "key3": [],\n' +
                '  "key4": {}\n' +
                '}', textOnly)
    }

    private static String withCapturedOutput(Closure code) {
        def capture = new CaptureOutput()
        try {
            ConsoleOutputs.add(capture)
            code()

            return capture.textOnly
        } finally {
            ConsoleOutputs.remove(capture)
        }
    }

    private static class CaptureOutput implements ConsoleOutput {
        private def stylesAndOutput = []
        private def textLInes = []

        @Override
        void out(Object... styleOrValues) {
            def line = Arrays.asList(styleOrValues).findAll {
                !(it instanceof Color) && !(it instanceof FontStyle)
            }.join('')

            textLInes.add(line)
            stylesAndOutput.addAll(Arrays.asList(styleOrValues))
        }

        String getTextOnly() {
            return textLInes.join('\n')
        }

        @Override
        void err(Object... styleOrValues) {
        }
    }
}
