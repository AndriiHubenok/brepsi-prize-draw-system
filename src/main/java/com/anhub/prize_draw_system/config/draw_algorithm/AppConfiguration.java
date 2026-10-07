package com.anhub.prize_draw_system.config.draw_algorithm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "app_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppConfiguration {

    @Id
    @Column(name = "key", length = 64)
    private String key;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
