package be.pxl.articles.controller;

import be.pxl.articles.domain.Notification;
import be.pxl.articles.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @MessageMapping("/text")
    public String sendNotification() throws InterruptedException {
        Thread.sleep(1000); // simulated delay
        return "Greetings from notification service";
//        notificationService.sendWebSocketNotification(notification);
    }
}
