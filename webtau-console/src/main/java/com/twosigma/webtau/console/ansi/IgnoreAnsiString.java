package com.twosigma.webtau.console.ansi;

import java.util.stream.Stream;

public class IgnoreAnsiString {
    private StringBuilder stringBuilder;

    public IgnoreAnsiString(Stream<?> styleOrValues) {
        this.stringBuilder = new StringBuilder();
        styleOrValues.forEach(this::append);
    }

    public IgnoreAnsiString(Object... styleOrValues) {
        this(Stream.of(styleOrValues));
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private void append(Object styleOrValue) {
        if (styleOrValue instanceof Color || styleOrValue instanceof FontStyle) {
            return;
        }

        stringBuilder.append(styleOrValue == null ? "null" : styleOrValue.toString());
    }
}
