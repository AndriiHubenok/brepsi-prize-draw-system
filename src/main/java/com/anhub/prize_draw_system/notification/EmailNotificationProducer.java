package com.anhub.prize_draw_system.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPrizeNotification(PrizeNotificationEvent event) {
        try {
            rabbitTemplate.convertAndSend(NotificationConfig.NOTIFICATION_EXCHANGE,
                    NotificationConfig.NOTIFICATION_ROUTING_KEY,
                    event);
            log.info("Sent prize notification for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send prize notification for email: {}", event.getEmail(), e);
        }
    }
}
