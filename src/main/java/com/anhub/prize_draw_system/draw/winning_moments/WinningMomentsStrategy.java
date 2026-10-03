package com.anhub.prize_draw_system.draw.winning_moments;

import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import com.anhub.prize_draw_system.draw.exceptions.IncorrectPromoCode;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.draw.PrizeDrawStrategy;
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
public class WinningMomentsStrategy implements PrizeDrawStrategy {

    private final CryptoPromoCodeService cryptoPromoCodeService;
    private final PromoCodeService promoCodeService;
    private final PrizeRepository prizeRepository;

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

        Prize prize = prizeRepository.tryWinPrize(currentTime).orElse(null);
        if (prize == null) {
            return Optional.empty();
        }

        prize.setStatus(Status.CLAIMED);
        prize.setClaimedTime(currentTime);
        prize.setPromoCode(promoCode);
        prize.setWinnerUserId(userId);
        prizeRepository.save(prize);

        return Optional.of(prize);
    }

    @Override
    public DrawAlgorithmType getType() {
        return DrawAlgorithmType.WINNING_MOMENTS;
    }
}
