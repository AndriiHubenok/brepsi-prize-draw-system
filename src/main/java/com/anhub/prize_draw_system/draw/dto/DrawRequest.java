package com.anhub.prize_draw_system.draw.dto;

import com.anhub.prize_draw_system.draw.enumerated.DesiredArticle;
import com.anhub.prize_draw_system.draw.enumerated.Supermarket;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DrawRequest {

    @NotBlank(message = "Promo code is required")
    private String promoCode;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Supermarket is required")
    private Supermarket supermarket;

    @NotBlank(message = "Desired prize is required")
    private DesiredArticle desiredPrize;
}
