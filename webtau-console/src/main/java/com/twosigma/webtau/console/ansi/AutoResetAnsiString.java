package com.twosigma.webtau.console.ansi;

import java.util.stream.Stream;

public class AutoResetAnsiString {
    private StringBuilder stringBuilder;

    public AutoResetAnsiString(Stream<?> styleOrValues) {
        this.stringBuilder = new StringBuilder();
        styleOrValues.forEach(this::append);
        reset();
    }

    public AutoResetAnsiString(Object... styleOrValues) {
        this(Stream.of(styleOrValues));
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private void append(Object styleOrValue) {
        stringBuilder.append(styleOrValue == null ? "null" : styleOrValue.toString());
    }

    private void reset() {
        stringBuilder.append(FontStyle.NORMAL); // TODO use reset
    }
}
