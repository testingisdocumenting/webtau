package com.twosigma.webtau.documentation;

public class Person {
    private String id;
    private int level;
    private int monthsAtCompany;

    public Person(String id, int level, int monthsAtCompany) {
        this.id = id;
        this.level = level;
        this.monthsAtCompany = monthsAtCompany;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getMonthsAtCompany() {
        return monthsAtCompany;
    }
}
