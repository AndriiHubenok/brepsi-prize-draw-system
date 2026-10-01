package com.anhub.prize_draw_system.prizes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    @Query("SELECT p FROM Prize p WHERE p.status = 'AVAILABLE' AND p.releaseTime < :currentTime ORDER BY p.releaseTime ASC LIMIT 1")
    public Optional<Prize> tryWinPrize(Instant currentTime);
}
