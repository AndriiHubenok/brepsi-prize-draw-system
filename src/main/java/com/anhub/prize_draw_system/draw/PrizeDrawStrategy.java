package com.anhub.prize_draw_system.draw;

import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.promocodes.CryptoPromoCodeService;

import java.util.Optional;

public interface PrizeDrawStrategy {

    Optional<Prize> tryWinPrize(DrawRequest drawRequest);

    DrawAlgorithmType getType();
}
