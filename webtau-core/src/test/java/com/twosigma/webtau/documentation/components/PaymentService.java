package com.twosigma.webtau.documentation.components;

public interface PaymentService {
    int availableBalance(String walletId);
    void makePayment(String walletId, int amount);
}
