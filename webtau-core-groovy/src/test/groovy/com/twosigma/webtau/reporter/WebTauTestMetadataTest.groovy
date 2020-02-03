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

package com.twosigma.webtau.reporter

import org.junit.Test

class WebTauTestMetadataTest {
    @Test
    void "should return previously registered values"() {
        def meta = new WebTauTestMetadata()

        def previous = meta.add([k1: 'v1', k2: 'v2', k3: 'v3'])
        previous.should == [:]

        previous = meta.add([k2: 'v2_', k3: 'v3_'])
        previous.should == [k2: 'v2', k3: 'v3']
    }

    @Test
    void "should provide access to values"() {
        def meta = new WebTauTestMetadata()
        meta.add([k1: 'v1', k2: 'v2', k3: 'v3'])

        meta.get('k1').should == 'v1'
        meta.k1.should == 'v1'
    }
}
