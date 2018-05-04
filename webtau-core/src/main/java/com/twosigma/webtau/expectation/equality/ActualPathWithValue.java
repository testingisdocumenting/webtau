package com.twosigma.webtau.expectation.equality;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.utils.StringUtils;

public class ActualPathWithValue {
    private ActualPath actualPath;
    private Object value;
    private String fullMessage;

    public ActualPathWithValue(ActualPath actualPath, Object value) {
        this.actualPath = actualPath;
        this.value = value;
        this.fullMessage = StringUtils.concatWithIndentation(actualPath.getPath() + ": ", DataRenderers.render(value));
    }

    public ActualPath getActualPath() {
        return actualPath;
    }

    public Object getValue() {
        return value;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    @Override
    public String toString() {
        return getFullMessage();
    }
}
