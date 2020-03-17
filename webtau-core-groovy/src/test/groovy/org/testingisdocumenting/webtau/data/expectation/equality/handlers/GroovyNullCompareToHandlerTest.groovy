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

package com.twosigma.webtau.data.expectation.equality.handlers

import org.codehaus.groovy.runtime.NullObject
import org.junit.Test

import static com.twosigma.webtau.WebTauCore.*

class GroovyNullCompareToHandlerTest {
    @Test
    void "should handle groovy null as actual"() {
        def handler = new GroovyNullCompareToHandler()
        assert handler.handleEquality(NullObject.getNullObject(), null)
    }

    @Test
    void "should delegate to the java null handler"() {
        actual(NullObject.getNullObject()).should(equal(null))

        code {
            actual(NullObject.getNullObject()).shouldNot(equal(null))
        } should throwException(AssertionError)
    }
}
