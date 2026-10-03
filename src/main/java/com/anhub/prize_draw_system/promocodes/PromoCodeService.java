package com.anhub.prize_draw_system.promocodes;

import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import com.anhub.prize_draw_system.promocodes.exceptions.AlreadyActivatedPromoCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final ActivatedPromoCodeRepository activatedPromoCodeRepository;

    public void checkAndActivatePromoCode(String promoCode, Long serial, String userId,
                                          Instant activatedAt, DesiredArticle desiredArticle, Supermarket supermarket) {

        boolean isActivated = activatedPromoCodeRepository.existsBySerialId(serial);
        if (isActivated) {
            throw new AlreadyActivatedPromoCode(promoCode);
        }

        ActivatedPromoCode activatedPromoCode = new ActivatedPromoCode();
        activatedPromoCode.setCode(promoCode);
        activatedPromoCode.setSerialId(serial);
        activatedPromoCode.setUserId(userId);
        activatedPromoCode.setActivatedAt(activatedAt);
        activatedPromoCode.setDesiredArticle(desiredArticle);
        activatedPromoCode.setSupermarket(supermarket);

        activatedPromoCodeRepository.save(activatedPromoCode);
    }
}
