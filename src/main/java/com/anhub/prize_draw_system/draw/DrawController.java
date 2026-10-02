package com.anhub.prize_draw_system.draw;

import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.dto.PrizeDTO;
import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.prizes.Voucher;
import com.anhub.prize_draw_system.prizes.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/draw")
@RequiredArgsConstructor
public class DrawController {

    private final PrizeDrawStrategy prizeDrawStrategy;
    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<PrizeDTO> draw(@RequestBody DrawRequest drawRequest) {
        Optional<Prize> prize = prizeDrawStrategy.tryWinPrize(drawRequest);
        PrizeDTO prizeDTO = new PrizeDTO();

        if (prize.isEmpty()) {
            String voucherCode = voucherService.getVoucherCode(drawRequest);

            prizeDTO.setName("VOUCHER");
            prizeDTO.setCode(voucherCode);

        } else {
            prizeDTO.setName(prize.get().getCategory().toString());
            prizeDTO.setCode(prize.get().getCode());
        }

        return ResponseEntity.ok(prizeDTO);
    }
}
