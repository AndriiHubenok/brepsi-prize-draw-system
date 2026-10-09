package com.anhub.prize_draw_system.draw;

import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.draw.dto.PrizeDTO;
import com.anhub.prize_draw_system.notification.EmailNotificationProducer;
import com.anhub.prize_draw_system.notification.PrizeNotificationEvent;
import com.anhub.prize_draw_system.prizes.Prize;
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

    private final PrizeDrawOrchestrator prizeDrawOrchestrator;
    private final VoucherService voucherService;
    private final EmailNotificationProducer emailNotificationProducer;

    @PostMapping
    public ResponseEntity<PrizeDTO> draw(@RequestBody DrawRequest drawRequest) {
        Optional<Prize> prize = prizeDrawOrchestrator.executeDraw(drawRequest);
        PrizeDTO prizeDTO = new PrizeDTO();

        if (prize.isEmpty()) {
            String voucherCode = voucherService.getVoucherCode(drawRequest);

            prizeDTO.setName("VOUCHER");
            prizeDTO.setCode(voucherCode);

        } else {
            prizeDTO.setName(prize.get().getCategory().toString());
            prizeDTO.setCode(prize.get().getCode());
        }

        PrizeNotificationEvent event = new PrizeNotificationEvent();
        event.setClaimUrl("https://github.com/AndriiHubenok/brepsi-prize-draw-system");
        event.setEmail(drawRequest.getEmail());
        event.setVoucherCode(prizeDTO.getCode());
        if (prizeDTO.getName().equals("VOUCHER")) {
            event.setGrandPrize(false);
            event.setPrizeName("10-€ Voucher");
        } else {
            event.setGrandPrize(true);
        }
        if (prizeDTO.getName().equals("CAP")) {
            event.setPrizeName("Cap");
        } else if (prizeDTO.getName().equals("T-SHIRT")) {
            event.setPrizeName("T-Shirt");
        } else if (prizeDTO.getName().equals("SHOPPER")) {
            event.setPrizeName("Shopper Bag");
        } else if (prizeDTO.getName().equals("SOCKS")) {
            event.setPrizeName("Socks");
        }
        emailNotificationProducer.sendPrizeNotification(event);

        return ResponseEntity.ok(prizeDTO);
    }
}
