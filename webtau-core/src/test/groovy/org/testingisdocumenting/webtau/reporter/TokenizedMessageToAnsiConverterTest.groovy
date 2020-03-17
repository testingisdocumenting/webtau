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

package org.testingisdocumenting.webtau.reporter

import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.console.ansi.FontStyle
import org.junit.Test

class TokenizedMessageToAnsiConverterTest {
    @Test
    void "should convert to ansi string based on registered tokens"() {
        def converter = new TokenizedMessageToAnsiConverter()
        converter.associate("keyword", false, FontStyle.BOLD, Color.CYAN)
        converter.associate("id", false, Color.BLUE)
        converter.associate("id2", false, FontStyle.BOLD, Color.BLUE)

        def message = new TokenizedMessage()
        message.add("keyword", "hello").add("id", "world").add("id2", "world")

        def valuesAndStyles = converter.convert(message)
        assert new AutoResetAnsiString(valuesAndStyles.stream()).toString() == "\u001B[1m\u001B[36mhello\u001B[34mworld\u001B[1m\u001B[34mworld\u001B[0m"
    }

    @Test
    void "should separate tokens with space if specified"() {
        def converter = new TokenizedMessageToAnsiConverter()
        converter.associate("keyword", false, FontStyle.BOLD, Color.CYAN)
        converter.associate("id", true, Color.BLUE)
        converter.associate("id2", true, FontStyle.BOLD, Color.BLUE)

        def message = new TokenizedMessage()
        message.add("keyword", "hello").add("id", "world").add("id2", "world")

        def valuesAndStyles = converter.convert(message)
        assert new AutoResetAnsiString(valuesAndStyles.stream()).toString() == "\u001B[1m\u001B[36mhello\u001B[34mworld \u001B[1m\u001B[34mworld\u001B[0m"
    }
}
