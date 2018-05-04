package com.twosigma.webtau.documentation.components;

public class Account {
    private String id;
    private String walletId;
    private String address;

    public Account(String id, String walletId, String address) {
        this.id = id;
        this.walletId = walletId;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getAddress() {
        return address;
    }
}
