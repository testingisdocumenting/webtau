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

package org.testingisdocumenting.webtau.data

import org.junit.Test
import org.testingisdocumenting.webtau.pdf.Pdf
import org.testingisdocumenting.webtau.utils.ResourceUtils

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.data.Data.data

class DataPdfTest {
    @Test
    void "read pdf from resource"() {
        Pdf pdf = data.pdf.read("sample.pdf")
        pdf.pageText(0).should contain('Test text paragraph Test')
    }

    @Test
    void "read pdf from bytes"() {
        byte[] pdfContent = pdfBinaryContent()
        Pdf pdf = data.pdf.read(pdfContent)
        pdf.pageText(0).should contain('Test text paragraph Test')
    }

    private static byte[] pdfBinaryContent() {
        ResourceUtils.binaryContent("sample.pdf")
    }
}
