package com.anhub.prize_draw_system.promocodes;

import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "activated_promo_codes")
@Data
@NoArgsConstructor
public class ActivatedPromoCode {

    @Id
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "serial_id", nullable = false, unique = true)
    private Long serialId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "activated_at", nullable = false)
    private Instant activatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "desired_article", nullable = false, length = 50)
    private DesiredArticle desiredArticle;

    @Enumerated(EnumType.STRING)
    @Column(name = "supermarket", nullable = false, length = 50)
    private Supermarket supermarket;
}
