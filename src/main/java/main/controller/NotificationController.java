package main.controller;

import main.entity.Notification;
import main.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/notification")
public class NotificationController {

  @Autowired
  NotificationService notificationService;

  @PostMapping("/processNotification")
  public String processNotification(@RequestBody Notification notification) {
    notificationService.acknowledgeNotification(notification);
    return "Notification processed";
  }
}
