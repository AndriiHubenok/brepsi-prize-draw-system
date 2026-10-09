package com.anhub.prize_draw_system.notification;

import com.anhub.prize_draw_system.prizes.Prize;
import com.anhub.prize_draw_system.prizes.enumerated.Category;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final MailgunMessagesApi mailgunMessagesApi;

    @Value("${mailgun.from-name:Brepsi Promo}")
    private String fromName;

    private static final String BANNER_URL = "https://i.ytimg.com/vi/z1dJ5D-8i7c/sddefault.jpg";

    public void sendPrizeEmailNotification(PrizeNotificationEvent event) {
        String subject = event.isGrandPrize()
                ? String.format("Congratulations! You won a %s! 🎁", event.getPrizeName())
                : "Congratulations! You’ve won a €10 Voucher! 🎉";

        String textBody = event.isGrandPrize()
                ? buildGrandPrizeText(event)
                : buildConsolationPrizeText(event);

        String htmlBody = event.isGrandPrize()
                ? buildGrandPrizeHtml(event)
                : buildConsolationPrizeHtml(event);

        Dotenv dotenv = Dotenv.load();
        String mailgunDomain = dotenv.get("MAILGUN_DOMAIN");

        Message message = Message.builder()
                .from(String.format("%s <no-reply@%s>", fromName, mailgunDomain))
                .to(event.getEmail())
                .subject(subject)
                .text(textBody)
                .html(htmlBody)
                .build();

        try {
            MessageResponse response = mailgunMessagesApi.sendMessage(mailgunDomain, message);
            log.info("Email sent successfully to {}. Response id: {}", event.getEmail(), response.getId());
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", event.getEmail(), e.getMessage(), e);
        }
    }

    // --- HTML pattern for grand prize (Merch) ---
    private String buildGrandPrizeHtml(PrizeNotificationEvent event) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="utf-8"></head>
            <body style="font-family: Arial, sans-serif; color: #111; max-width: 600px; margin: 0 auto; padding: 20px;">
                <img src="%s" alt="Brepsi - Taste The Jora" style="width: 100%%; border-radius: 8px; display: block; margin-bottom: 24px;">
                <h1 style="font-size: 32px; font-weight: 800; margin-bottom: 16px;">Congratulations!</h1>
                <p style="font-size: 16px; line-height: 1.5;">
                    You have won a cool <strong>%s</strong> from Brepsi!
                </p>
                <p style="font-size: 16px;">
                    Your personal winning code: <span style="background-color: #ffeb3b; padding: 3px 8px; font-weight: bold; font-family: monospace; font-size: 18px;">%s</span>
                </p>
                
                <h3 style="font-size: 18px; margin-top: 24px;">How to claim your prize:</h3>
                <ol style="font-size: 15px; line-height: 1.6; padding-left: 20px;">
                    <li>Click the link to visit the shop: <a href="%s" style="color: #004B93; text-decoration: underline;">%s</a></li>
                    <li>Add the product to your cart.</li>
                    <li>Enter your delivery address and your winning code at checkout, and order your prize with <strong>free shipping by October 31, 2026</strong>!</li>
                </ol>

                <p style="font-size: 15px; margin-top: 24px;">
                    We hope you enjoy your new fashion piece!<br>
                    <strong>Your Brepsi Team</strong>
                </p>
                <hr style="border: none; border-top: 1px solid #e0e0e0; margin-top: 30px;">
                <p style="font-size: 13px; color: #666;">
                    P.S. If you have any questions, Brepsi Support is available anytime at: 
                    <a href="mailto:service@brepsi.de" style="color: #666;">service@brepsi.de</a>
                </p>
            </body>
            </html>
            """.formatted(BANNER_URL, event.getPrizeName(), event.getVoucherCode(), event.getClaimUrl(), event.getClaimUrl());
    }

    // --- HTML pattern for consolation prize (10 €) ---
    private String buildConsolationPrizeHtml(PrizeNotificationEvent event) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="utf-8"></head>
            <body style="font-family: Arial, sans-serif; color: #111; max-width: 600px; margin: 0 auto; padding: 20px;">
                <img src="%s" alt="Brepsi - Taste The Jora" style="width: 100%%; border-radius: 8px; display: block; margin-bottom: 24px;">
                <h1 style="font-size: 32px; font-weight: 800; margin-bottom: 16px;">Congratulations!</h1>
                <p style="font-size: 16px; line-height: 1.5;">
                    You have won a <strong>€10 voucher</strong> from Brepsi!
                </p>
                
                <h3 style="font-size: 18px; margin-top: 24px;">How to redeem your voucher:</h3>
                <ol style="font-size: 15px; line-height: 1.6; padding-left: 20px;">
                    <li>Click the following link: <a href="%s" style="color: #004B93; text-decoration: underline;">Apply €10 Discount</a></li>
                    <li>Choose your cool fashion piece (minimum order value €30).</li>
                    <li>Order by <strong>December 31, 2026</strong>!</li>
                </ol>

                <p style="font-size: 15px; margin-top: 24px;">
                    We hope you enjoy your €10 voucher!<br>
                    <strong>Your Brepsi Team</strong>
                </p>
                <hr style="border: none; border-top: 1px solid #e0e0e0; margin-top: 30px;">
                <p style="font-size: 13px; color: #666;">
                    P.S. If you have any questions, Brepsi Support is available anytime at: 
                    <a href="mailto:service@brepsi.de" style="color: #666;">service@brepsi.de</a>
                </p>
            </body>
            </html>
            """.formatted(BANNER_URL, event.getClaimUrl());
    }

    private String buildGrandPrizeText(PrizeNotificationEvent event) {
        return String.format(
                "Congratulations!\n\n" +
                        "You have won a cool %s from Brepsi!\n" +
                        "Your personal winning code is: %s\n\n" +
                        "How to claim your prize:\n" +
                        "1. Click the following link: %s\n" +
                        "2. Add your product to the cart.\n" +
                        "3. Enter your address and winning code at checkout to order with free shipping by October 31, 2026!\n\n" +
                        "We hope you enjoy your new fashion piece!\nYour Brepsi Team\n\n" +
                        "P.S. Questions? Contact service@brepsi.de",
                event.getPrizeName(), event.getVoucherCode(), event.getClaimUrl()
        );
    }

    private String buildConsolationPrizeText(PrizeNotificationEvent event) {
        return String.format(
                "Congratulations!\n\n" +
                        "You have won a €10 voucher from Brepsi!\n\n" +
                        "How to redeem your voucher:\n" +
                        "1. Click the following link: %s\n" +
                        "2. Choose your fashion piece (minimum order value €30).\n" +
                        "3. Order by December 31, 2026!\n\n" +
                        "We hope you enjoy your €10 voucher!\nYour Brepsi Team\n\n" +
                        "P.S. Questions? Contact service@brepsi.de",
                event.getClaimUrl()
        );
    }
}