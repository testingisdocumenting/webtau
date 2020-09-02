/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http.render

import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.console.ansi.FontStyle
import org.testingisdocumenting.webtau.http.datanode.DataNodeBuilder
import org.testingisdocumenting.webtau.http.datanode.DataNodeId
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.equal

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
        def ansi = captureAnsiOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromList(new DataNodeId("root"), [1, 2, 3, 4]))
        }

        ansi.should == [Color.YELLOW, '[', '\n',
                        '  ', Color.CYAN, '1', Color.YELLOW, ',', '\n',
                        '  ', Color.CYAN, '2', Color.YELLOW, ',', '\n',
                        '  ', Color.CYAN, '3', Color.YELLOW, ',', '\n',
                        '  ', Color.CYAN, '4', '\n',
                        Color.YELLOW, ']', '\n']
    }

    @Test
    void "should print object data node"() {
        def textOnly = captureTextOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                    key1: 'value1',
                    key2: 'value2',
                    key3: [key31: 'value31', key32: [5, 6, 8]],
                    key4: [key41: 'value41', key42: 'value42']]))
        }

        textOnly.should == '{\n' +
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
                '}'
    }

    @Test
    void "should limit output to display a specified number of lines"() {
        def textOnly = captureTextOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                    key1: 'value1',
                    key2: 'value2',
                    key3: [key31: 'value31', key32: [5, 6, 8]],
                    key4: [key41: 'value41', key42: 'value42']]), 5)
        }

        textOnly.should == '{\n' +
                '  "key1": "value1",\n' +
                '...\n' +
                '    "key42": "value42"\n' +
                '  }\n' +
                '}'
    }

    @Test
    void "should print all the lines if specified number of lines greater than or equal actual"() {
        def textOnly = captureTextOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                    key1: 'value1',
                    key2: 'value2',
                    key3: [key31: 'value31', key32: [5, 6, 8]],
                    key4: [key41: 'value41', key42: 'value42']]), 16)
        }

        textOnly.should == '{\n' +
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
                '}'
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
        } catch (AssertionError ignored) {
            // catch as it is an expected here and serves to check if the value will be rendered as failed
        }

        def textOnly = captureTextOutput {
            new DataNodeAnsiPrinter().print(dataNode)
        }

        textOnly.should == '[\n' +
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
                ']'
    }

    @Test
    void "should collapse empty list and object"() {
        def textOnly = captureTextOutput {
            new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                    key1: 'value1',
                    key2: 'value2',
                    key3: [],
                    key4: [[:], [:]],
                    key5: [[], []],
                    key6: [:]]))
        }

        textOnly.should == '{\n' +
                '  "key1": "value1",\n' +
                '  "key2": "value2",\n' +
                '  "key3": [],\n' +
                '  "key4": [\n' +
                '    {},\n' +
                '    {}\n' +
                '  ],\n' +
                '  "key5": [\n' +
                '    [],\n' +
                '    []\n' +
                '  ],\n' +
                '  "key6": {}\n' +
                '}'
    }

    private static String captureTextOutput(Closure code) {
        return captureOutput(code).textOnly
    }

    private static List<Object> captureAnsiOutput(Closure code) {
        return captureOutput(code).stylesAndOutput
    }

    private static CaptureOutput captureOutput(Closure code) {
        def capture = new CaptureOutput()
        try {
            ConsoleOutputs.add(capture)
            code()

            return capture
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
            stylesAndOutput.add('\n')
        }

        String getTextOnly() {
            return textLInes.join('\n')
        }

        @Override
        void err(Object... styleOrValues) {
        }
    }
}
