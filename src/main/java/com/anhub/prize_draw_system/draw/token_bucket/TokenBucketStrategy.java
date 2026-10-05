package com.anhub.prize_draw_system.draw.token_bucket;

import com.anhub.prize_draw_system.draw.PrizeDrawStrategy;
import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import com.anhub.prize_draw_system.draw.exceptions.IncorrectPromoCode;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.prizes.PrizeRepository;
import com.anhub.prize_draw_system.prizes.enumerated.Status;
import com.anhub.prize_draw_system.promocodes.CryptoPromoCodeService;
import com.anhub.prize_draw_system.promocodes.PromoCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenBucketStrategy implements PrizeDrawStrategy {

    private final CryptoPromoCodeService cryptoPromoCodeService;
    private final PromoCodeService promoCodeService;
    private final PrizeRepository prizeRepository;
    private final TokenBucket tokenBucket;

    @Override
    @Transactional
    public synchronized Optional<Prize> tryWinPrize(DrawRequest drawRequest) {

        String promoCode = drawRequest.getPromoCode();

        Long serial = cryptoPromoCodeService.validateAndExtractSerial(promoCode);
        if (serial == null) {
            throw new IncorrectPromoCode(promoCode);
        }

        String userId = (drawRequest.getName() + drawRequest.getSurname() + drawRequest.getEmail())
                .toLowerCase();
        Instant currentTime = Instant.now();
        DesiredArticle desiredArticle = drawRequest.getDesiredArticle();
        Supermarket supermarket = drawRequest.getSupermarket();
        promoCodeService.checkAndActivatePromoCode(promoCode, serial, userId,
                currentTime, desiredArticle, supermarket);

        if (!tokenBucket.tryConsume()) {
            return Optional.empty();
        }

        return prizeRepository.claimAvailablePrize(userId, promoCode, currentTime);
    }

    @Override
    public DrawAlgorithmType getType() {
        return DrawAlgorithmType.TOKEN_BUCKET;
    }
}
