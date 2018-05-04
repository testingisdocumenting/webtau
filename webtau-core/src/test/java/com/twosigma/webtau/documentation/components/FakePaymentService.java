package com.twosigma.webtau.documentation.components;

import java.util.List;

public class FakePaymentService implements PaymentService {
    private static final TestCrystals nullCrystals = new TestCrystals("", 0);
    private List<TestCrystals> testCrystals;

    public FakePaymentService(List<TestCrystals> testCrystals) {
        this.testCrystals = testCrystals;
    }

    @Override
    public int availableBalance(String walletId) {
        return findById(walletId).getAmount();
    }

    @Override
    public void makePayment(String walletId, int amount) {
        TestCrystals testCrystals = findById(walletId);
        testCrystals.setAmount(testCrystals.getAmount() - amount);
    }

    private TestCrystals findById(String walletId) {
        return testCrystals.stream().filter(a -> a.getWalletId().equals(walletId))
                .findFirst().orElse(nullCrystals);
    }
}
