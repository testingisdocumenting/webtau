/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class MapMatchersGroovyExamplesTest {
    @Test
    void equalityMismatch() {
        code {
            // maps-equal-mismatch
            Map<String, ?> generated = generate()
            generated.should == [firstName: "G-FN", lastName: "G-LN",
                    address: [street: "generated-street", city: "GenCity", zipCode: "12345"]]
            // maps-equal-mismatch
        } should throwException(AssertionError)
    }

    private static Map<String, ?> generate() {
        return map("firstName", "G-FN", "lastName", "G-LN",
                "address", map("street", "generated-street", "city", "GenSity"))
    }
}
