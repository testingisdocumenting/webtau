package com.twosigma.webtau.documentation.components;

public class Game {
    private String title;
    private int basePrice;
    private int discountPercent;

    public Game(String title, int basePrice, int discountPercent) {
        this.title = title;
        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
    }

    public String getTitle() {
        return title;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountedPrice() {
        return basePrice * discountPercent / 100;
    }

    @Override
    public String toString() {
        return title + " (" + getDiscountedPrice() + " crystals)";
    }
}
