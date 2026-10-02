package com.anhub.prize_draw_system.promocodes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivatedPromoCodeRepository extends JpaRepository<ActivatedPromoCode, Long> {

    boolean existsBySerialId(Long serialId);
}
