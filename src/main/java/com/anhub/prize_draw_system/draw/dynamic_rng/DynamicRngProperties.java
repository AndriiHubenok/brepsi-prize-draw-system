package com.anhub.prize_draw_system.draw.dynamic_rng;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "promo.dynamic-rng")
public class DynamicRngProperties {
    private int expectedDailyTraffic = 10000;
    private double minProbability = 0.005;
    private double maxProbability = 0.20;
}
