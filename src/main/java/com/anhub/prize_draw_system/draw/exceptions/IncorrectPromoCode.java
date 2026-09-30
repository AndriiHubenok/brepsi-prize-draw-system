package com.anhub.prize_draw_system.draw.exceptions;

public class IncorrectPromoCode extends RuntimeException {
    public IncorrectPromoCode(String promoCode) {
        super("Incorrect promo code: " + promoCode);
    }
}
