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

package com.twosigma.webtau.pdf

import com.twosigma.webtau.http.HttpRequestHeader
import com.twosigma.webtau.http.config.HttpConfiguration
import com.twosigma.webtau.http.config.HttpConfigurations
import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.http.testserver.TestServerBinaryResponse
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.http.Http.http
import static com.twosigma.webtau.pdf.Pdf.pdf

class PdfHttpTest implements HttpConfiguration {
    static TestServer testServer = new TestServer()

    private static final int PORT = 7823

    @BeforeClass
    static void startServer() {
        testServer.start(PORT)

        testServer.registerGet("/report",
                new TestServerBinaryResponse(ResourceUtils.binaryContent("report.pdf")))
    }

    @AfterClass
    static void stopServer() {
        testServer.stop()
    }

    @Before
    void initCfg() {
        HttpConfigurations.add(this)
    }

    @After
    void cleanCfg() {
        HttpConfigurations.remove(this)
    }

    @Test
    void "download pdf and assert page text using contains"() {
        http.get("/report") {
            pdf(body).pageText(0).should contain('Quarterly earnings:')
        }
    }

    @Test
    void "download pdf and assert page text using equal and contains"() {
        http.get("/report") {
            def pdf = pdf(body)
            pdf.pageText(0).should contain('Quarterly earnings:')
            pdf.pageText(1).should == 'Intentional blank page\n'
        }
    }

    @Override
    String fullUrl(String url) {
        return "http://localhost:${PORT}${url}"
    }

    @Override
    HttpRequestHeader fullHeader(String fullUrl, String passedUrl, HttpRequestHeader given) {
        return given
    }
}
