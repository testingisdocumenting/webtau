/*
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

package org.testingisdocumenting.webtau.pdf

import org.testingisdocumenting.webtau.data.traceable.CheckLevel
import org.testingisdocumenting.webtau.data.traceable.TraceableValue
import org.testingisdocumenting.webtau.http.datanode.DataNodeId
import org.testingisdocumenting.webtau.http.datanode.StructuredDataNode
import org.testingisdocumenting.webtau.utils.ResourceUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.pdf.Pdf.pdf

class PdfTest {
    static byte[] pdfContent = ResourceUtils.binaryContent('sample.pdf')

    @Test
    void "should extract text from a page by index"() {
        def pdf = pdf(ResourceUtils.binaryContent('sample.pdf'))
        pdf.pageText(0).should contain('Test text paragraph Test')
    }

    @Test
    void "should mark binary node as fuzzy passed when pdf is successfully parsed"() {
        def node = new StructuredDataNode(new DataNodeId('body'), new TraceableValue(pdfContent))

        pdf(node)
        node.getTraceableValue().checkLevel.should == CheckLevel.FuzzyPassed
    }

    @Test
    void "should mark binary node as failed when pdf cannot be parsed"() {
        def node = new StructuredDataNode(new DataNodeId('body'), new TraceableValue([1, 2, 3] as byte[]))

        code {
            pdf(node)
        } should throwException(AssertionError, ~/Failed to parse pdf: java.io.IOException:/)

        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitFailed
    }
}
