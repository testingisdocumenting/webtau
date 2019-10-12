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

package com.twosigma.webtau.junit5

import org.junit.jupiter.api.Test

import static com.twosigma.webtau.Ddjt.*

class DynamicTestsTest {
    @Test
    void "converts table rows to dynamic test instances"() {
        def dynamicTests = DynamicTests.fromTable('display name prefix',
                table('columnA', 'columnB').values(
                             10, 20,
                             30, 40)
        ) {
            println it
        }

        def asList = dynamicTests.collect { it }
        asList.displayName.should == ['display name prefix: {columnA=10, columnB=20}',
                                      'display name prefix: {columnA=30, columnB=40}']
    }

    @Test
    void "should use label as display text if present"() {
        def dynamicTests = DynamicTests.fromTable('prefix',
                table('label', 'columnA', 'columnB').values(
                        'test 1', 10, 20,
                        'test 2', 30, 40)
        ) {
            println it
        }

        def asList = dynamicTests.collect { it }
        asList.displayName.should == ['prefix: test 1', 'prefix: test 2']
    }
}
