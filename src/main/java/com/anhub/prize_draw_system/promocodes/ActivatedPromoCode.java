package com.anhub.prize_draw_system.promocodes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Integer serialId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "activated_at", nullable = false)
    private Instant activatedAt;
}
