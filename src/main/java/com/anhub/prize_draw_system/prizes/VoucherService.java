package com.anhub.prize_draw_system.prizes;

import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.prizes.enumerated.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    @Transactional
    public synchronized String getVoucherCode(DrawRequest drawRequest) {

        Voucher voucher = voucherRepository.getRandomVoucher()
                .orElseThrow(() -> new RuntimeException("No voucher available"));

        String userId = (drawRequest.getName() + drawRequest.getSurname() + drawRequest.getEmail())
                .toLowerCase();
        voucher.setStatus(Status.CLAIMED);
        voucher.setClaimedTime(Instant.now());
        voucher.setWinnerUserId(userId);
        voucher.setPromoCode(drawRequest.getPromoCode());
        voucherRepository.save(voucher);

        return voucher.getCode();
    }
}
