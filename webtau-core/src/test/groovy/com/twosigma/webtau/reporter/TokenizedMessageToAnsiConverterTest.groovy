package com.twosigma.webtau.reporter

import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.console.ansi.FontStyle
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

        def ansiString = converter.convert(message)
        assert ansiString.toString() == "\u001B[1m\u001B[36mhello\u001B[34mworld\u001B[1m\u001B[34mworld\u001B[0m"
    }

    @Test
    void "should separate tokens with space if specified"() {
        def converter = new TokenizedMessageToAnsiConverter()
        converter.associate("keyword", false, FontStyle.BOLD, Color.CYAN)
        converter.associate("id", true, Color.BLUE)
        converter.associate("id2", true, FontStyle.BOLD, Color.BLUE)

        def message = new TokenizedMessage()
        message.add("keyword", "hello").add("id", "world").add("id2", "world")

        def ansiString = converter.convert(message)
        assert ansiString.toString() == "\u001B[1m\u001B[36mhello\u001B[34mworld \u001B[1m\u001B[34mworld\u001B[0m"
    }
}
