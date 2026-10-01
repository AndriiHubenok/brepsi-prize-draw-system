package com.anhub.prize_draw_system.prizes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query(nativeQuery=true, value="SELECT *  FROM voucher_pool WHERE status = 'AVAILABLE' ORDER BY random() LIMIT 1")
    Optional<Voucher> getRandomVoucher();
}
