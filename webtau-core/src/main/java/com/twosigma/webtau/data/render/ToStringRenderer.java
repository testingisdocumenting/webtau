package com.twosigma.webtau.data.render;

public class ToStringRenderer implements DataRenderer {
    @Override
    public String render(Object data) {
        return data.toString();
    }
}
