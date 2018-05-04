package com.twosigma.webtau.documentation;

public class PersonSummary {
    private String id;
    private boolean mobilityRequired;

    public PersonSummary(String id, boolean mobilityRequired) {
        this.id = id;
        this.mobilityRequired = mobilityRequired;
    }

    public String getId() {
        return id;
    }

    public boolean isMobilityRequired() {
        return mobilityRequired;
    }
}
