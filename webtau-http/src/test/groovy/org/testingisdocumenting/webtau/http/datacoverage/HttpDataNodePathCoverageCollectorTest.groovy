/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datacoverage

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.webtau.data.traceable.CheckLevel
import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.data.datanode.DataNodeBuilder
import org.testingisdocumenting.webtau.data.datanode.DataNodeId
import org.testingisdocumenting.webtau.http.request.EmptyRequestBody
import org.testingisdocumenting.webtau.http.validation.BodyDataNode
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult
import org.testingisdocumenting.webtau.reporter.WebTauReport
import org.testingisdocumenting.webtau.reporter.WebTauReportName
import org.testingisdocumenting.webtau.reporter.WebTauTestList

class HttpDataNodePathCoverageCollectorTest implements ConsoleOutput {
    def lines = []

    @Before
    void setup() {
        lines.clear()
        ConsoleOutputs.add(this)

        HttpDataNodePathCoverageCollector.coverageByOperationId.clear()
    }

    @After
    void cleanup() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "report untouched fields with more label"() {
        def collector = new HttpDataNodePathCoverageCollector()

        def op = createHttpResult("GET", "/url", 7)

        collector.validate(op)
        collector.generate(new WebTauReport(new WebTauReportName("tests report", "/url"), new WebTauTestList(), 0l, 100l))

        lines.should == ["Data Coverage HTTP routes that have non validated response fields",
                         "  GET /url",
                         "    root.key2",
                         "    root.key3",
                         "    root.key4",
                         "    ...(3 more)"]
    }

    @Test
    void "report untouched fields without more label"() {
        def collector = new HttpDataNodePathCoverageCollector()

        def op = createHttpResult("GET", "/url", 6)
        op.getBodyNode().get("key1").traceableValue.updateCheckLevel(CheckLevel.FuzzyPassed)

        collector.validate(op)
        collector.generate(new WebTauReport(new WebTauReportName("tests report", "/url"), new WebTauTestList(), 0l, 100l))

        lines.should == ["Data Coverage HTTP routes that have non validated response fields",
                         "  GET /url",
                         "    root.key2",
                         "    root.key3",
                         "    root.key4",
                         "    root.key5",
                         "    root.key6"]
    }

    @Test
    void "report multiple operations with more label"() {
        def collector = new HttpDataNodePathCoverageCollector()

        def operations = [
                createHttpResult("GET", "/url1", 2),
                createHttpResult("GET", "/url2", 2),
                createHttpResult("GET", "/url3", 2),
                createHttpResult("GET", "/url4", 2),
                createHttpResult("GET", "/url5", 2),
                createHttpResult("GET", "/url6", 2)
        ]

        operations.each { collector.validate(it) }

        collector.generate(new WebTauReport(new WebTauReportName("tests report", "/url"), new WebTauTestList(), 0l, 100l))

        lines.should == ["Data Coverage HTTP routes that have non validated response fields",
                         "  GET /url1",
                         "    root.key2",
                         "  GET /url2",
                         "    root.key2",
                         "  GET /url3",
                         "    root.key2",
                         "  ...(3 more operations)"]
    }

    @Test
    void "displays all operations if below a threshold"() {
        def collector = new HttpDataNodePathCoverageCollector()

        def operations = [
                createHttpResult("GET", "/url1", 2),
                createHttpResult("GET", "/url2", 2),
                createHttpResult("GET", "/url3", 2),
                createHttpResult("GET", "/url4", 2),
        ]

        operations.each { collector.validate(it) }

        collector.generate(new WebTauReport(new WebTauReportName("tests report", "/url"), new WebTauTestList(), 0l, 100l))

        lines.should == ["Data Coverage HTTP routes that have non validated response fields",
                         "  GET /url1",
                         "    root.key2",
                         "  GET /url2",
                         "    root.key2",
                         "  GET /url3",
                         "    root.key2",
                         "  GET /url4",
                         "    root.key2"]
    }

    static HttpValidationResult createHttpResult(String method, String url, numberOfFields) {
        def operation = new HttpValidationResult("", method, url, url, new HttpHeader(), new EmptyRequestBody())
        operation.setOperationId(method + " " + url)

        def map = [:]
        numberOfFields.times {
            map["key${it + 1}"] = "value${it + 1}"
        }

        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), map)
        node.get("key1").traceableValue.updateCheckLevel(CheckLevel.FuzzyPassed)

        operation.setResponseBodyNode(new BodyDataNode(new HttpResponse(), node))

        return operation
    }

    @Override
    void out(Object... styleOrValues) {
        println new AutoResetAnsiString(styleOrValues)
        lines.add(new IgnoreAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {

    }
}
