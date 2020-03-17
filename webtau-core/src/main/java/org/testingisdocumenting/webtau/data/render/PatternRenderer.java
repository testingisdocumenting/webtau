package com.twosigma.webtau.data.render;

import java.util.regex.Pattern;

public class PatternRenderer implements DataRenderer {
    @Override
    public String render(Object data) {
        return data instanceof Pattern ?
                "pattern /" + data + "/" :
                null;
    }
}
