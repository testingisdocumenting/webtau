package com.twosigma.webtau.reporter;

public class ReportToken {
    private String type;
    private String textRepresentation;

    public ReportToken(String type, String textRepresentation) {
        this.type = type;
        this.textRepresentation = textRepresentation;
    }

    public String getType() {
        return type;
    }

    public String getTextRepresentation() {
        return textRepresentation;
    }
}
