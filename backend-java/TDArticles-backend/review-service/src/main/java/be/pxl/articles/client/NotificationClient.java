package be.pxl.articles.client;

import be.pxl.articles.controller.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/")
    void sendNotification(@RequestBody NotificationRequest notification);
}
