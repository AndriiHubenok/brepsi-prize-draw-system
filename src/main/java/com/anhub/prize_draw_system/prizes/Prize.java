package com.anhub.prize_draw_system.prizes;

import com.anhub.prize_draw_system.prizes.enumerated.Category;
import com.anhub.prize_draw_system.prizes.enumerated.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "prize_pool")
@Data
@NoArgsConstructor
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private Status status = Status.AVAILABLE;

    @Column(name = "release_time", nullable = false)
    private Instant releaseTime;

    @Column(name = "winner_user_id")
    private String winnerUserId;

    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "claimed_time")
    private Instant claimedTime;
}
