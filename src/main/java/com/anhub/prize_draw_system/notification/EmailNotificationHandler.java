package com.anhub.prize_draw_system.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationHandler {

    private final EmailNotificationService emailNotificationService;

    @RabbitListener(queues = NotificationConfig.NOTIFICATION_QUEUE)
    public void handlePrizeNotification(PrizeNotificationEvent event) {
        try {
            emailNotificationService.sendPrizeEmailNotification(event);
            log.info("Handled prize notification for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to handle prize notification for email: {}", event.getEmail(), e);
        }
    }
}
