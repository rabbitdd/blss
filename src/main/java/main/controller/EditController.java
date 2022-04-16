package main.controller;

import main.entity.Request;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("wiki")
public class EditController {

  @Autowired ValidationService validationService;

  @Autowired SearchService searchService;

  @Autowired EditService editService;

  @Autowired PageService pageService;

  @Autowired
  ApproveService approveService;

  @PostMapping("/edit")
  public String editArticle(@RequestBody Request request) {
    if (validationService.validation(request)) {
      Long page = searchService.getPageByName(request.getPage().getName());
      if (editService.addChange(page, request.getComment())) {
        return "Successfully added, expect a verdict";
      } else {
        return "one version is already under review";
      }
    } else {
      return "bad data";
    }
  }

  @GetMapping("/getStatus")
  public String getStatus(@RequestParam String login, @RequestParam Long id){

    if(!validationService.validator(login, id)){
      return "no access";
    }
    else {
        return editService.getStatus(id);
    }
  }

  @PostMapping("/editWithApprove")
  public boolean editWithApprove(Request request) {
    return editService.editWithApprove(request);
  }

  @PostMapping("/approve")
  public boolean approveEditPageForOneUser(@RequestParam Long userId, @RequestParam Long senderId) {
    return editService.approveEditPageForOneUser(userId, senderId);
  }
}
