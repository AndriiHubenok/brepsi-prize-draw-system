package com.anhub.prize_draw_system.draw.token_bucket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

@Component
public class TokenBucket {

    private final double capacity;
    private final double refillRatePerSecond;

    private double currentTokens;
    private Instant lastRefillTimestamp;

    public TokenBucket(@Value("${token.bucket.capacity:3.0}") double capacity,
                       @Value("${token.bucket.refill-rate-per-second:0.00567}") double refillRatePerSecond) {

        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.currentTokens = 0.0;
        this.lastRefillTimestamp = Instant.now();
    }

    public synchronized boolean tryConsume() {

        refillTokens();
        if (currentTokens >= 1.0) {
            currentTokens -= 1.0;
            return true;
        }

        return false;
    }

    private synchronized void refillTokens() {

        // Refill the token bucket based on the time elapsed since the last refill
        Instant now = Instant.now();
        double elapsedSeconds = Duration.between(lastRefillTimestamp, now).toMillis() / 1000.0;

        // If no time has passed, no tokens need to be added
        if (elapsedSeconds > 0) {
            double tokensToAdd = elapsedSeconds * refillRatePerSecond;
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(LocalTime.of(0, 0)) && currentTime.isBefore(LocalTime.of(6, 0))) {
                tokensToAdd *= 0.03;
            }

            this.currentTokens = Math.min(capacity, this.currentTokens + tokensToAdd);
            this.lastRefillTimestamp = now;
        }
    }

}
