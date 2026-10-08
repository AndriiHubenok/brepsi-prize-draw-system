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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WinningMomentsStrategy implements PrizeDrawStrategy {

    private final PromoCodeService promoCodeService;
    private final PrizeRepository prizeRepository;

    @Override
    @Transactional
    public synchronized Optional<Prize> tryWinPrize(DrawRequest drawRequest) {

        Instant currentTime = Instant.now();
        promoCodeService.checkAndActivatePromoCode(drawRequest, currentTime);

        Prize prize = prizeRepository.tryWinPrize(currentTime).orElse(null);
        if (prize == null) {
            return Optional.empty();
        }

        prize.setStatus(Status.CLAIMED);
        prize.setClaimedTime(currentTime);
        prize.setPromoCode(drawRequest.getPromoCode());
        prize.setWinnerUserId(drawRequest.getName() + drawRequest.getSurname() + drawRequest.getEmail());
        prizeRepository.save(prize);
        log.info("Win by algorithm: {}, User: {}", getType().toString(), prize.getWinnerUserId());

        return Optional.of(prize);
    }

    @Override
    public DrawAlgorithmType getType() {
        return DrawAlgorithmType.WINNING_MOMENTS;
    }
}
