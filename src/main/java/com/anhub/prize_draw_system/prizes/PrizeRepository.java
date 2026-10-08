package com.anhub.prize_draw_system.prizes;

import com.anhub.prize_draw_system.prizes.enumerated.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    @Query("""
             SELECT p FROM Prize p WHERE p.status = 'AVAILABLE' AND p.releaseTime < :currentTime
                         ORDER BY p.releaseTime ASC LIMIT 1
            """)
    Optional<Prize> tryWinPrize(Instant currentTime);

    @Query(value = """
            UPDATE prize_pool
            SET status = 'CLAIMED',
                winner_user_id = :userId,
                promo_code = :promoCode,
                claimed_time = :claimedTime
            WHERE id = (
                SELECT id FROM prize_pool
                WHERE status = 'AVAILABLE'
                ORDER BY release_time ASC
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            )
            RETURNING *
            """, nativeQuery = true)
    Optional<Prize> claimAvailablePrize(String userId, String promoCode, Instant claimedTime);

    @Query("""
    SELECT count(p) FROM Prize p 
    WHERE p.status = 'AVAILABLE' 
      AND p.releaseTime <= :endOfToday
    """)
    int countAvailablePrizesForToday(@Param("endOfToday") Instant endOfToday);
}
