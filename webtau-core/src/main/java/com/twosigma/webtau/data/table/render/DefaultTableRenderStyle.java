package com.twosigma.webtau.data.table.render;

public class DefaultTableRenderStyle implements TableRenderStyle {
    @Override
    public String headerMidLeft() {
        return ":";
    }

    @Override
    public String headerMidMid() {
        return "|";
    }

    @Override
    public String headerMidRight() {
        return ":";
    }

    public String headerBotLeft() {
        return ".";
    }

    public String headerBotMid() {
        return ".";
    }

    public String headerBotRight() {
        return ".";
    }

    public String headerBotFill() {
        return "_";
    }

    @Override
    public String bodyMidLeft() {
        return "|";
    }

    @Override
    public String bodyMidMid() {
        return "|";
    }

    @Override
    public String bodyMidRight() {
        return "|";
    }

    @Override
    public String bodyBotLeft() {
        return ".";
    }

    @Override
    public String bodyBotMid() {
        return ".";
    }

    @Override
    public String bodyBotRight() {
        return "|";
    }

    @Override
    public String bodyBotFill() {
        return "_";
    }
}
