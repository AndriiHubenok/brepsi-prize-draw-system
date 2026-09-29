package com.anhub.prize_draw_system.prizes;

import com.anhub.prize_draw_system.prizes.enumerated.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "voucher_pool")
@Data
@NoArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 6)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private Status status = Status.AVAILABLE;

    @Column(name = "winner_user_id")
    private String winnerUserId;

    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "claimed_time")
    private Instant claimedTime;
}
