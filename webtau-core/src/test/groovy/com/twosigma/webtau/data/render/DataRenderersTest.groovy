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

package com.twosigma.webtau.data.render

import org.junit.Test

class DataRenderersTest {
    @Test
    void "null values should be rendered as a String"() {
        assert new NullRenderer().render(null) == "[null]"
        assert DataRenderers.render(null) == "[null]"
    }

    @Test
    void "null renderer should not handle non-null values"() {
        assert new NullRenderer().render("some object") == null
    }

    @Test
    void "strings are rendered with quotes"() {
        assert DataRenderers.render("some object") == "\"some object\""
    }

    @Test
    void "regex are rendered as patterns"() {
        assert DataRenderers.render(~/some stuff/) == "pattern /some stuff/"
    }
}
