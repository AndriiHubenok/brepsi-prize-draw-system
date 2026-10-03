package com.anhub.prize_draw_system.admin.dto;

import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesiredArticleStatisticsDTO {
    private DesiredArticle desiredArticle;
    private long count;
}
