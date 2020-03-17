/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.data.components;

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
