package com.anhub.prize_draw_system.draw;

import com.anhub.prize_draw_system.draw.dto.DrawRequest;
import com.anhub.prize_draw_system.prizes.Prize;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public Optional<Prize> draw(@RequestBody DrawRequest drawRequest) {
        Optional<Prize> prize = prizeDrawStrategy.tryWinPrize(drawRequest);
        return Optional.empty();

    }
}
