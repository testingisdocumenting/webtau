package com.twosigma.webtau.console.ansi;

public enum FontStyle {
    NORMAL("\u001B[0m"),
    BOLD("\u001B[1m");

    private final String code;

    FontStyle(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
