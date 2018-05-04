package com.twosigma.webtau.data.render

import org.junit.Test

class NullRendererTest {
    @Test
    void "null values should be rendered as a String"() {
        assert new NullRenderer().render(null) == "[null]"
    }

    @Test
    void "should not handle non-null values"() {
        assert new NullRenderer().render("some object") == null
    }
}
