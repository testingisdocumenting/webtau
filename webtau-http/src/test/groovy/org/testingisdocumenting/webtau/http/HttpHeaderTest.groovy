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

package org.testingisdocumenting.webtau.http

import org.junit.Test

class HttpHeaderTest {
    @Test
    void "renders header as key = value"() {
        def header = new HttpHeader(['Content-Type': 'application/json', 'Authorization': 'Bearer'])
        header.toString().should == 'Content-Type: application/json\n' +
            'Authorization: Bearer'
    }

    @Test
    void "should redact authorization and cookies header values"() {
        def header = new HttpHeader([
                'Content-Type': 'application/json',
                'Authorization': 'Bearer',
                'Custom': 'Value',
                'Cookie': 'sweet',
                'Set-Cookie': 'will be sweet'])

        header.redactSecrets().toListOfMaps().should == ['key'         | 'value'           ] {
                                                      ______________________________________
                                                        'Content-Type' | 'application/json'
                                                       'Authorization' | '................'
                                                              'Custom' | 'Value'
                                                              'Cookie' | '................'
                                                          'Set-Cookie' | '................' }
    }

    @Test
    void "case insensitive get ignores case"() {
        def header = new HttpHeader([
            'Location': 'http://foo'
        ])
        header.caseInsensitiveGet('Location').should == 'http://foo'
        header.caseInsensitiveGet('location').should == 'http://foo'
        header.caseInsensitiveGet('lOcAtIoN').should == 'http://foo'
        header.caseInsensitiveGet('LOCATION').should == 'http://foo'
    }

    @Test
    void "merge with map"() {
        def header = new HttpHeader([
            'foo': 'bar'
        ])
        def mergedHeader = header.merge(['other': 'value'])
        mergedHeader.should == new HttpHeader([
            'foo': 'bar',
            'other': 'value'
        ])
    }

    @Test
    void "merge with header"() {
        def header = new HttpHeader([
            'foo': 'bar'
        ])
        def otherHeader = new HttpHeader([
            'other': 'value'
        ])
        def mergedHeader = header.merge(otherHeader)
        mergedHeader.should == new HttpHeader([
            'foo': 'bar',
            'other': 'value'
        ])
    }

    @Test
    void "build from empty header"() {
        def header = HttpHeader.EMPTY
        def newHeader = header.with('foo', 'bar')
        newHeader.should == new HttpHeader(['foo': 'bar'])
    }

    @Test
    void "build from existing header with a single new value"() {
        def header = new HttpHeader([k: 'v'])
        def newHeader = header.with('foo', 'bar')
        newHeader.should == new HttpHeader([k: 'v', 'foo': 'bar'])
    }

    @Test
    void "build from existing header with a new map"() {
        def header = new HttpHeader([k: 'v'])
        def newHeader = header.with([k1: 'v1', k2: 'v2'])
        newHeader.should == new HttpHeader([k: 'v', k1: 'v1', k2: 'v2'])
    }

    @Test
    void "get returns null if the key is not in the map"() {
        def header = new HttpHeader([:])
        header.get('noSuchKey').should == null
    }

    @Test
    void "caseInsensitiveGet returns null if the key is not in the map"() {
        def header = new HttpHeader([:])
        header.caseInsensitiveGet('noSUchKey').should == null
    }

}
