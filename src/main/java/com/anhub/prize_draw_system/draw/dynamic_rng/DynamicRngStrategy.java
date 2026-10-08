package com.anhub.prize_draw_system.draw.dynamic_rng;

import com.anhub.prize_draw_system.draw.PrizeDrawStrategy;
import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.prizes.PrizeRepository;
import com.anhub.prize_draw_system.promocodes.PromoCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicRngStrategy implements PrizeDrawStrategy {

    private final PromoCodeService promoCodeService;
    private final PrizeRepository prizeRepository;
    private final DynamicProbabilityEngine dynamicProbabilityEngine;

    @Override
    public Optional<Prize> tryWinPrize(DrawRequest drawRequest) {

        Instant currentTime = Instant.now();
        promoCodeService.checkAndActivatePromoCode(drawRequest, currentTime);

        double currentProbability = dynamicProbabilityEngine.calculateCurrentProbability();
        if (currentProbability <= 0.0) {
            return Optional.empty();
        }

        double roll = ThreadLocalRandom.current().nextDouble();

        if (roll >= currentProbability) {
            return Optional.empty();
        }

        String userId = drawRequest.getName() + drawRequest.getSurname() + drawRequest.getEmail();
        Optional<Prize> claimedPrize = prizeRepository.claimAvailablePrize(
                drawRequest.getName() + drawRequest.getSurname() + drawRequest.getEmail(),
                drawRequest.getPromoCode(),
                currentTime
        );

        if (claimedPrize.isPresent()) {
            dynamicProbabilityEngine.decrementPrizeCache();
            log.info("Win by algorithm: {}, User: {}, Chance: {}", getType().toString(), userId, currentProbability);
        }

        return claimedPrize;
    }

    @Override
    public DrawAlgorithmType getType() {
        return DrawAlgorithmType.DYNAMIC_RNG;
    }
}
