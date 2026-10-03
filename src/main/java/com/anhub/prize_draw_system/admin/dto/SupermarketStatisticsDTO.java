package com.anhub.prize_draw_system.admin.dto;

import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupermarketStatisticsDTO {
    private Supermarket supermarket;
    private long count;
}
