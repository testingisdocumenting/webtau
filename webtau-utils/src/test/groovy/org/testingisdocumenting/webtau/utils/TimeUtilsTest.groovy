/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.utils

import org.junit.Test

class TimeUtilsTest {
    @Test
    void "should only render mills when below 1000 ms"() {
        assert TimeUtils.renderMillisHumanReadable(26) == '26ms'
    }

    @Test
    void "should render seconds when over 1000 ms"() {
        assert TimeUtils.renderMillisHumanReadable(1234) == '1s 234ms'
    }
}
