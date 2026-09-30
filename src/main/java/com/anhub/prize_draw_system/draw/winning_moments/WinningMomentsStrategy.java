package com.anhub.prize_draw_system.draw.winning_moments;

import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.exceptions.IncorrectPromoCode;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.draw.PrizeDrawStrategy;
import com.anhub.prize_draw_system.promocodes.CryptoPromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WinningMomentsStrategy implements PrizeDrawStrategy {

    private final CryptoPromoCodeService cryptoPromoCodeService;

    @Override
    public Optional<Prize> tryWinPrize(DrawRequest drawRequest) {
        String promoCode = drawRequest.getPromoCode();

        Long serial = cryptoPromoCodeService.validateAndExtractSerial(promoCode);
        if (serial == null) {
            throw new IncorrectPromoCode(promoCode);
        }

        String userId = (drawRequest.getName() + drawRequest.getSurname() + drawRequest.getEmail())
                .toLowerCase();

        return null;
    }

    @Override
    public DrawAlgorithmType getType() {
        return DrawAlgorithmType.WINNING_MOMENTS;
    }
}
