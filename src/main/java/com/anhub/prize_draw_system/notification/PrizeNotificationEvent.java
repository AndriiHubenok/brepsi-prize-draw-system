package com.anhub.prize_draw_system.notification;

import com.anhub.prize_draw_system.prizes.Prize;
import lombok.Data;

@Data
public class PrizeNotificationEvent {
    private String email;
    private String prizeName;
    private String voucherCode;
    private String claimUrl;
    private boolean isGrandPrize;
}
