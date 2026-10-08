package com.anhub.prize_draw_system.draw.dynamic_rng;

import com.anhub.prize_draw_system.prizes.PrizeRepository;
import com.anhub.prize_draw_system.prizes.enumerated.Status;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicProbabilityEngine {

    private final PrizeRepository prizeRepository;
    private final DynamicRngProperties dynamicRngProperties;

    private final AtomicInteger cachedRemainingPrizes = new AtomicInteger(0);

    private static final ZoneId CAMPAIGN_ZONE = ZoneId.of("Europe/Berlin");

    @PostConstruct
    @Scheduled(fixedRate = 60_000)
    public void syncRemainingPrizesCache() {

        Instant endOfToday = LocalDate.now(CAMPAIGN_ZONE)
                .atTime(LocalTime.MAX)
                .atZone(CAMPAIGN_ZONE)
                .toInstant();

        int availableToday = prizeRepository.countAvailablePrizesForToday(endOfToday);
        cachedRemainingPrizes.set(availableToday);
    }

    public void decrementPrizeCache() {
        cachedRemainingPrizes.decrementAndGet();
    }

    public double calculateCurrentProbability() {

        int remainingPrizes = cachedRemainingPrizes.get();
        if (remainingPrizes <= 0) {
            return 0.0;
        }

        int secondsPassedToday = LocalTime.now().toSecondOfDay();
        int secondsInDay = 24 * 60 * 60;
        double remainingDayFraction = Math.max(0.01, (secondsInDay - secondsPassedToday) / (double) secondsInDay);

        double expectedRemainingTraffic = dynamicRngProperties.getExpectedDailyTraffic() * remainingDayFraction;
        double rawProbability = remainingPrizes / expectedRemainingTraffic;

        return Math.clamp(rawProbability,
                dynamicRngProperties.getMinProbability(), dynamicRngProperties.getMaxProbability());
    }
}
