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

package com.twosigma.webtau.http

import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

class HttpUrlTest {
    @Test
    void "detects if url is a full url"() {
        HttpUrl.isFull("http://localhost:3030/abc").should == true
        HttpUrl.isFull("https://localhost:3030/abc").should == true
        HttpUrl.isFull("file://localhost:3030/abc").should == true
        HttpUrl.isFull("mailto://test.account").should == true

        HttpUrl.isFull("/relative-url").should == false
        HttpUrl.isFull("relative-url").should == false
    }

    @Test
    void "url concatenation validates passed urls to be non null"() {
        code {
            HttpUrl.concat(null, '/relative')
        } should throwException(IllegalArgumentException, 'passed url on the left is NULL')

        code {
            HttpUrl.concat('/relative', null)
        } should throwException(IllegalArgumentException, 'passed url on the right is NULL')
    }

    @Test
    void "url concatenation handles slashes to avoid double slash or slash absense"() {
        def expected = 'https://base/relative'

        HttpUrl.concat('https://base/', '/relative').should == expected
        HttpUrl.concat('https://base', '/relative').should == expected
        HttpUrl.concat('https://base/', 'relative').should == expected
        HttpUrl.concat('https://base', 'relative').should == expected
    }

    @Test
    void "extract path from a given url"() {
        HttpUrl.extractPath('relative/path').should == 'relative/path'
        HttpUrl.extractPath('https://localhost:8080/relative/path').should == '/relative/path'
    }

    @Test
    void "validates full url before before extracting path portion"() {
        code {
            HttpUrl.extractPath('garbage://localhost:8080/relative/path').should == '/relative/path'
        } should throwException(IllegalArgumentException, 'invalid url: garbage://localhost:8080/relative/path')
    }
}
