package com.anhub.prize_draw_system;

import com.anhub.prize_draw_system.prizes.Prize;

public interface PrizeDrawStrategy {

    Prize tryWinPrize(String userId, String promoCode);

    DrawAlgorithmType getType();
}
