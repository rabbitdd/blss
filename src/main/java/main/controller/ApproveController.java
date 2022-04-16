package main.controller;

import main.entity.Verdict;
import main.service.ApproveService;
import main.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/approve")
public class ApproveController {
  @Autowired ApproveService approveService;
  @Autowired ValidationService validationService;

  @PostMapping("/verdict")
  public Boolean makeVerdict(@RequestBody Verdict verdict) {
    long index = validationService.changeToAdminValidation(verdict);
    if (index != -1) {
      return approveService.approve(verdict, index);
    } else {
      return false;
    }
  }

  @GetMapping("/getToVerdict")
  public String getToVerdict(@RequestParam String login) {
    if (!validationService.userHasValidRole(login)) return "no access";
    else {
      String string = approveService.getToApprove(login);
      if (string.length() == 0) {
        return "noting to check";
      } else {
        return string;
      }
    }
  }
}
