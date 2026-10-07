package com.anhub.prize_draw_system.draw;

import com.anhub.prize_draw_system.config.draw_algorithm.AppConfiguration;
import com.anhub.prize_draw_system.config.draw_algorithm.AppConfigurationRepository;
import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.enumerated.DrawAlgorithmType;
import com.anhub.prize_draw_system.prizes.Prize;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrizeDrawOrchestrator {

    private static final String CONFIG_KEY = "DRAW_ALGORITHM";

    private final AppConfigurationRepository configRepository;
    private final Map<DrawAlgorithmType, PrizeDrawStrategy> strategies;

    private final AtomicReference<DrawAlgorithmType> currentAlgorithm =
            new AtomicReference<>(DrawAlgorithmType.WINNING_MOMENTS);

    public PrizeDrawOrchestrator(
            AppConfigurationRepository configRepository,
            List<PrizeDrawStrategy> strategyList
    ) {
        this.configRepository = configRepository;
        this.strategies = strategyList.stream()
                .collect(Collectors.toUnmodifiableMap(
                        PrizeDrawStrategy::getType,
                        Function.identity()
                ));
    }

    /**
     * By the time the application starts, the database is already initialized, so we can read the current algorithm from the DB and store it in memory for fast access.
     */
    @PostConstruct
    public void init() {
        configRepository.findById(CONFIG_KEY).ifPresentOrElse(
                cfg -> {
                    try {
                        DrawAlgorithmType loaded = DrawAlgorithmType.valueOf(cfg.getValue());
                        currentAlgorithm.set(loaded);
                        log.info("Loaded active draw algorithm: {}", loaded);
                    } catch (IllegalArgumentException e) {
                        log.warn("Unknown draw algorithm in DB: {}. Using default.", cfg.getValue());
                    }
                },
                () -> log.info("Draw algorithm configuration not found in DB, using: {}", currentAlgorithm.get())
        );
    }

    /**
     * Called by the controller when a user attempts to participate in the draw. It delegates the request to the currently active strategy.
     */
    public Optional<Prize> executeDraw(DrawRequest request) {
        DrawAlgorithmType activeType = currentAlgorithm.get();
        PrizeDrawStrategy strategy = strategies.get(activeType);

        if (strategy == null) {
            throw new IllegalStateException("Strategy not found for type: " + activeType);
        }

        return strategy.tryWinPrize(request);
    }

    /**
     * Switches the active draw algorithm. This method is transactional to ensure that the change is persisted in the database and reflected in memory atomically.
     */
    @Transactional
    public DrawAlgorithmType switchAlgorithm(DrawAlgorithmType newAlgorithm) {
        if (!strategies.containsKey(newAlgorithm)) {
            throw new IllegalArgumentException("Algorithm not implemented in the system: " + newAlgorithm);
        }

        AppConfiguration config = configRepository.findById(CONFIG_KEY)
                .orElseGet(() -> new AppConfiguration(CONFIG_KEY, newAlgorithm.name(), Instant.now()));
        config.setValue(newAlgorithm.name());
        config.setUpdatedAt(Instant.now());
        configRepository.save(config);

        currentAlgorithm.set(newAlgorithm);
        log.info("Draw algorithm successfully switched to: {}", newAlgorithm);

        return newAlgorithm;
    }

    public DrawAlgorithmType getCurrentAlgorithm() {
        return currentAlgorithm.get();
    }
}
