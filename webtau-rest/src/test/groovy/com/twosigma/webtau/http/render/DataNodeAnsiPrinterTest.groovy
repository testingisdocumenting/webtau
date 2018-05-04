package com.twosigma.webtau.http.render

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import org.junit.AfterClass
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
        new DataNodeAnsiPrinter().print(DataNodeBuilder.fromList(new DataNodeId("root"), [1, 2, 3, 4]))
    }

    @Test
    void "should print object data node"() {
        new DataNodeAnsiPrinter().print(DataNodeBuilder.fromMap(new DataNodeId("root"), [
                key1: 'value1',
                key2: 'value2',
                key3: [key31: 'value31', key32: [5, 6, 8]],
                key4: [key41: 'value41', key42: 'value42']]))
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

        new DataNodeAnsiPrinter().print(dataNode)
    }
}
