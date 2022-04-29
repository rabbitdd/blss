package main.controller;

import main.entity.Notification;
import main.entity.Verdict;
import main.service.ApproveService;
import main.service.NotificationService;
import main.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.spi.http.HttpContext;
import java.util.Collections;
import java.util.List;

@RestController("/approve")
public class ApproveController {

  @Autowired ApproveService approveService;
  @Autowired ValidationService validationService;
  @Autowired NotificationService notificationService;

  @PostMapping("/verdict")
  public ResponseEntity<Verdict> approveEditPageForOneUser(@RequestBody Verdict verdict) {
    if (approveService
        .approve(verdict)
        .getResponseVerdictAns()
        .equals("Подтверждение прошло успешно"))
      return new ResponseEntity<>(verdict, HttpStatus.ACCEPTED);
    return new ResponseEntity<>(verdict, HttpStatus.NOT_ACCEPTABLE);
  }

  @GetMapping("/getAllNotifications")
  public ResponseEntity<?> getAllNotificationsStatus(@RequestParam String login) {
    return notificationService.getAllNotifications(login);
  }

  @GetMapping("getAllApprovePages")
  public ResponseEntity<?> getAllApprovePages(@RequestParam String login) {
    return approveService.getApprovePages(login);
  }
}
