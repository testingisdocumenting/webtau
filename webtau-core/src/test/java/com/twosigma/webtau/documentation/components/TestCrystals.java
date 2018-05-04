package com.twosigma.webtau.documentation.components;

public class TestCrystals {
    private String walletId;
    private int amount;

    public TestCrystals(String walletId, int amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
