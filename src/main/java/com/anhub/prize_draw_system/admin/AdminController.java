package com.anhub.prize_draw_system.admin;

import com.anhub.prize_draw_system.admin.dto.DesiredArticleStatisticsDTO;
import com.anhub.prize_draw_system.admin.dto.SupermarketStatisticsDTO;
import com.anhub.prize_draw_system.promocodes.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PromoCodeService promoCodeService;

    @GetMapping("/statistics/desired-articles")
    public ResponseEntity<List<DesiredArticleStatisticsDTO>> getDesiredArticlesStatistics() {
        return ResponseEntity.ok(promoCodeService.getDesiredArticlesStatistics());
    }

    @GetMapping("/statistics/supermarkets")
    public ResponseEntity<List<SupermarketStatisticsDTO>> getSupermarketStatistics() {
        return ResponseEntity.ok(promoCodeService.getSupermarketStatistics());
    }
}
