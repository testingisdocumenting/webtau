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

package com.twosigma.webtau.utils

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class UrlUtilsTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    void "detects if url is a full url"() {
        assert UrlUtils.isFull("http://localhost:3030/abc")
        assert UrlUtils.isFull("https://localhost:3030/abc")
        assert UrlUtils.isFull("file://localhost:3030/abc")
        assert UrlUtils.isFull("mailto://test.account")

        assert !UrlUtils.isFull("/relative-url")
        assert !UrlUtils.isFull("relative-url")
    }

    @Test
    void "url concatenation validates passed url on the left to be non null"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('passed url on the left is NULL')

        UrlUtils.concat(null, '/relative')
    }

    @Test
    void "url concatenation validates passed url on the right to be non null"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('passed url on the right is NULL')

        UrlUtils.concat('/relative', null)
    }

    @Test
    void "url concatenation handles slashes to avoid double slash or slash absense"() {
        def expected = 'https://base/relative'

        assert UrlUtils.concat('https://base/', '/relative') == expected
        assert UrlUtils.concat('https://base', '/relative') == expected
        assert UrlUtils.concat('https://base/', 'relative') == expected
        assert UrlUtils.concat('https://base', 'relative') == expected
    }

    @Test
    void "extract path from a given url"() {
        assert UrlUtils.extractPath('relative/path') == 'relative/path'
        assert UrlUtils.extractPath('https://localhost:8080/relative/path') == '/relative/path'
    }

    @Test
    void "validates full url before before extracting path portion"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('invalid url: garbage://localhost:8080/relative/path')

        assert UrlUtils.extractPath('garbage://localhost:8080/relative/path') == '/relative/path'
    }

    @Test
    void "extract query params from a given url"() {
        def expectedParams = [a: ['b'], c: ['d']]
        def expectedMultiParams = [a: ['b', 'c']]
        def expectedNullParams = [a: [null], b: ['c']]

        assert UrlUtils.extractQueryParams('relative/path?a=b&c=d') == expectedParams
        assert UrlUtils.extractQueryParams('https://localhost:8080/relative/path?a=b&c=d') == expectedParams
        assert UrlUtils.extractQueryParams('https://localhost:8080/relative/path?a=b&a=c') == expectedMultiParams
        assert UrlUtils.extractQueryParams('https://localhost:8080/relative/path?a[0]=b&a[1]=c') == expectedMultiParams

        assert UrlUtils.extractQueryParams('https://localhost:8080/relative/path?a&b=c') == expectedNullParams

        assert UrlUtils.extractQueryParams('relative/path') == [:]
        assert UrlUtils.extractQueryParams('https://localhost:8080/relative/path') == [:]
    }

    @Test
    void "validates full url before before extracting query params"() {
        exception.expect(IllegalArgumentException)
        exception.expectMessage('invalid url: garbage://localhost:8080/relative/path')

        UrlUtils.extractQueryParams('garbage://localhost:8080/relative/path').should == '/relative/path'
    }
}
