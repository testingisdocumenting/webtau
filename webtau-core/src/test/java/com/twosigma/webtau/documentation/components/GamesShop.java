package com.twosigma.webtau.documentation.components;

public class GamesShop {
    private PaymentService paymentService;
    private DeliveryService deliveryService;

    public GamesShop(PaymentService paymentService, DeliveryService deliveryService) {
        this.paymentService = paymentService;
        this.deliveryService = deliveryService;
    }

    public void buyGame(Account account, Game game) {
        int balance = paymentService.availableBalance(account.getWalletId());
        if (balance < game.getDiscountedPrice()) {
            throw new RuntimeException("Not enough crystals to buy game: " + game);
        }

        paymentService.makePayment(account.getWalletId(), game.getDiscountedPrice());
        deliveryService.deliver(game, account.getAddress());
    }
}
