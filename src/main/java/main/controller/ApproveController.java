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
    // return approveService.approve();
    return true;
  }

  @GetMapping("/getToVerdict")
  public String getToVerdict(@RequestParam String login) {
    // return approveService.getToApprove();
    return "";
  }
}
