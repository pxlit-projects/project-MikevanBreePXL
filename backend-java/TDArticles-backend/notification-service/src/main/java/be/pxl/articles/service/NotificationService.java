package be.pxl.articles.service;

import be.pxl.articles.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public void sendWebSocketNotification(Notification notification) {
        try {
            String destination = "/notifications";
            logger.info("Sending to user {} at destination {}", notification.getReceiver(), destination);
            messagingTemplate.convertAndSendToUser(
                    notification.getReceiver(),
                    destination,
                    notification
            );
            logger.info("Message sent successfully");
        } catch (Exception e) {
            logger.error("Failed to send notification:", e);
        }
    }

    @RabbitListener(queues = "NotificationsQueue")
    public void receiveNotification(Notification notification) {
        logger.info("Received notification from {}; contains text: {}; for: {}",
                notification.getSender(),
                notification.getMessage(),
                notification.getReceiver());
        sendWebSocketNotification(notification);
    }
}