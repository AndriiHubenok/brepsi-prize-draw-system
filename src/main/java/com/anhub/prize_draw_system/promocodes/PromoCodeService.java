package com.anhub.prize_draw_system.promocodes;

import com.anhub.prize_draw_system.promocodes.exceptions.AlreadyActivatedPromoCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final ActivatedPromoCodeRepository activatedPromoCodeRepository;

    public void checkAndActivatePromoCode(String promoCode, Long serial, String userId, Instant activatedAt) {

        boolean isActivated = activatedPromoCodeRepository.existsBySerialId(serial);
        if (isActivated) {
            throw new AlreadyActivatedPromoCode(promoCode);
        }

        ActivatedPromoCode activatedPromoCode = new ActivatedPromoCode();
        activatedPromoCode.setCode(promoCode);
        activatedPromoCode.setSerialId(serial);
        activatedPromoCode.setUserId(userId);
        activatedPromoCode.setActivatedAt(activatedAt);
        activatedPromoCodeRepository.save(activatedPromoCode);
    }
}
