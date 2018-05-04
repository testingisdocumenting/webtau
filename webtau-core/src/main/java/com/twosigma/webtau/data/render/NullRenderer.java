package com.twosigma.webtau.data.render;

public class NullRenderer implements DataRenderer {
    @Override
    public String render(Object data) {
        return data == null ? "[null]" : null;
    }
}
