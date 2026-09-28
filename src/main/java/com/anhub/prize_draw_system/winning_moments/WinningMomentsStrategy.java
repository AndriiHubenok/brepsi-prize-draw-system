package com.anhub.prize_draw_system.winning_moments;

import com.anhub.prize_draw_system.DrawAlgorithmType;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.PrizeDrawStrategy;
import org.springframework.stereotype.Component;

@Component
public class WinningMomentsStrategy implements PrizeDrawStrategy {

    @Override
    public Prize tryWinPrize(String userId, String promoCode) {
        return null;
    }

    @Override
    public DrawAlgorithmType getType() {
        return DrawAlgorithmType.WINNING_MOMENTS;
    }
}
