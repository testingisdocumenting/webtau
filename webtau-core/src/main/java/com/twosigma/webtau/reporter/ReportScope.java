package com.twosigma.webtau.reporter;

import java.util.ArrayList;
import java.util.List;

public class ReportScope {
    private String id;
    private List<ReportToken> report;

    private List<ReportScope> children;

    public ReportScope(String id) {
        this.id = id;
        this.report = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addToken(String type, String textRepresentation) {
        report.add(new ReportToken(type, textRepresentation));
    }

    public void addScope(ReportScope scope) {
        children.add(scope);
    }
}
