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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.junit.Test;

import java.util.Map;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class MapMatchersJavaExamplesTest {
    @Test
    public void equalityMismatch() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "maps-equal-console-output", """
                X failed expecting [value] to equal {
                                                      "firstName": "G-FN",
                                                      "lastName": "G-LN",
                                                      "address": {"street": "generated-street", "city": "GenCity", "zipCode": "12345"}
                                                    }:
                    mismatches:
                   \s
                    [value].address.city:  actual: "GenSity" <java.lang.String>
                                         expected: "GenCity" <java.lang.String>
                                                       ^
                   \s
                    missing, but expected values:
                   \s
                    [value].address.zipCode: "12345" (Xms)
                 \s
                  {
                    "firstName": "G-FN",
                    "lastName": "G-LN",
                    "address": {"street": "generated-street", "city": **"GenSity"**, "zipCode": <missing>}
                  }""", () -> {
            // maps-equal-mismatch
            Map<String, ?> generated = generate();
            actual(generated).should(equal(map("firstName", "G-FN", "lastName", "G-LN",
                    "address", map("street", "generated-street", "city", "GenCity", "zipCode", "12345"))));
            // maps-equal-mismatch
        });
    }

    @Test
    public void containMismatch() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "maps-contain-console-output", """
                X failed expecting [value] to contain {"firstName": "G-FN", "lastName": "G-LN", "middleName": "G-MD"}:
                    missing values:
                   \s
                    [value].middleName: "G-MD" (Xms)
                 \s
                  {
                    "firstName": "G-FN",
                    "lastName": "G-LN",
                    "address": {"street": "generated-street", "city": "GenSity"},
                    "middleName": **<missing>**
                  }""",() -> {
            // maps-contain-mismatch
            Map<String, ?> generated = generate();
            actual(generated).should(contain(
                    map("firstName", "G-FN", "lastName", "G-LN", "middleName", "G-MD")));
            // maps-contain-mismatch
        });
    }

    private static Map<String, ?> generate() {
        return map("firstName", "G-FN", "lastName", "G-LN",
                "address", map("street", "generated-street", "city", "GenSity"));
    }
}
