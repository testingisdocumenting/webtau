/*
 * Copyright 2022 webtau maintainers
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

package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class WarningJavaTest {
    @Test
    public void warningLabel() {
        // warning-label
        warning("warning message");
        // warning-label
    }

    @Test
    public void warningKeyValues() {
        // warning-map
        warning("warning message with map", aMapOf("k1", "v1", "k2", "v2"));
        // warning-map
        // warning-vararg
        warning("another warning message", "k3", "v3", "k4", "v4");
        // warning-vararg
    }
}
