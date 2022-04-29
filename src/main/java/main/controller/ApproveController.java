package main.controller;

import lombok.AllArgsConstructor;
import main.entity.Verdict;
import main.service.ApproveService;
import main.service.NotificationService;
import main.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController("/approve")
public class ApproveController {

  private final ApproveService approveService;
  private final NotificationService notificationService;

  @PostMapping("/verdict")
  public ResponseEntity<Verdict> approveEditPageForOneUser(@RequestBody Verdict verdict) {
    if (approveService
        .approve(verdict)
        .getResponseVerdictAns()
        .contains("Статус изменился"))
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
