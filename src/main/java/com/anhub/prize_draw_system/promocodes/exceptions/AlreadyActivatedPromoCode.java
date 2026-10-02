package com.anhub.prize_draw_system.promocodes.exceptions;

public class AlreadyActivatedPromoCode extends RuntimeException {
    public AlreadyActivatedPromoCode(String promoCode) {
        super("This promo code has already been activated: " + promoCode);
    }
}
