package com.anhub.prize_draw_system.promocodes;

import com.anhub.prize_draw_system.statistics.dto.DesiredArticleStatisticsDTO;
import com.anhub.prize_draw_system.statistics.dto.SupermarketStatisticsDTO;
import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import com.anhub.prize_draw_system.promocodes.exceptions.AlreadyActivatedPromoCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final ActivatedPromoCodeRepository activatedPromoCodeRepository;

    public void checkAndActivatePromoCode(String promoCode, Long serial, String userId,
                                          Instant activatedAt, DesiredArticle desiredArticle, Supermarket supermarket) {

        boolean isActivated = activatedPromoCodeRepository.existsBySerialId(serial);
        if (isActivated) {
            throw new AlreadyActivatedPromoCode(promoCode);
        }

        ActivatedPromoCode activatedPromoCode = new ActivatedPromoCode();
        activatedPromoCode.setCode(promoCode);
        activatedPromoCode.setSerialId(serial);
        activatedPromoCode.setUserId(userId);
        activatedPromoCode.setActivatedAt(activatedAt);
        activatedPromoCode.setDesiredArticle(desiredArticle);
        activatedPromoCode.setSupermarket(supermarket);

        activatedPromoCodeRepository.save(activatedPromoCode);
    }

    public List<DesiredArticleStatisticsDTO> getDesiredArticlesStatistics() {

        return activatedPromoCodeRepository.findAll().stream()
                .collect(Collectors.groupingBy(ActivatedPromoCode::getDesiredArticle, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    DesiredArticleStatisticsDTO dto = new DesiredArticleStatisticsDTO();
                    dto.setDesiredArticle(entry.getKey());
                    dto.setCount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<SupermarketStatisticsDTO> getSupermarketStatistics() {

        return activatedPromoCodeRepository.findAll().stream()
                .collect(Collectors.groupingBy(ActivatedPromoCode::getSupermarket, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    SupermarketStatisticsDTO dto = new SupermarketStatisticsDTO();
                    dto.setSupermarket(entry.getKey());
                    dto.setCount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
