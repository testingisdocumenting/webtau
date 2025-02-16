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

package org.example.domain;

import org.junit.Assert;
import org.junit.Test;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class CustomComplexDataTest {
    @Test
    public void complexDataEqualsEachOther() {
        CustomComplexData calculated = new CustomComplexData("cA", "cB");
        calculated.addRow(1, 2);
        calculated.addRow(3, 4);

        CustomComplexData expected = new CustomComplexData("cA", "cB");
        expected.addRow(1, 2);
        expected.addRow(3, 4);

        Assert.assertEquals(expected, calculated);
    }

    @Test
    public void complexDataEqualsMismatch() {
        // complex-data-fail-preparation
        CustomComplexData calculated = new CustomComplexData("cA", "cB");
        calculated.addRow(1, 2);
        calculated.addRow(3, 4);

        CustomComplexData expected = new CustomComplexData("cA", "cB");
        expected.addRow(1, 2);
        expected.addRow(3, 5);
        // complex-data-fail-preparation

        code(() -> {
            // default-assertion
            Assert.assertEquals(expected, calculated);
            // default-assertion
        }).should(throwException(AssertionError.class));
        doc.actual.capture("custom-complex-data-default-fail-output");
    }

    @Test
    public void complexDataEqualsEachOtherUsingWebTau() {
        CustomComplexData calculated = new CustomComplexData("cA", "cB");
        calculated.addRow(1, 2);
        calculated.addRow(3, 4);

        CustomComplexData expected = new CustomComplexData("cA", "cB");
        expected.addRow(1, 2);
        expected.addRow(3, 5);

        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "custom-complex-data-webtau-fail-output",
                """
                        X failed expecting [value] to equal cA │ cB
                                                             1 │  2
                                                             3 │  5:
                            [value][1].cB:  actual: 4 <java.lang.Integer>
                                          expected: 5 <java.lang.Integer> (Xms)
                         \s
                          cA │ cB  \s
                           1 │     2
                           3 │ **4**""", () -> {
                    // webtau-assertion
                    actual(calculated).should(equal(expected));
                    // webtau-assertion
                });
    }
}