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
  public boolean editArticle(@RequestBody Request request) {
    // todo любой пользователь может предложить правку
    return editService.editWithApprove(request);

  }

  @GetMapping("/getStatus")
  public String getStatus(@RequestParam String login, @RequestParam Long id){

    // todo получить статус по конкретному изменения в формате json
    return "";
//    if(!validationService.validator(login, id)){
//      return "no access";
//    }
//    else {
//        return editService.getStatus(id);
//    }
  }

//  @PostMapping("/editWithApprove")
//  public boolean editWithApprove(Request request) {
//    return editService.editWithApprove(request);
//  }

  @PostMapping("/approve")
  public boolean approveEditPageForOneUser(@RequestParam String userLogin, @RequestParam String senderLogin, @RequestParam String articleName) {
    // todo approveEditPageForOneUser();
    return true;
    // return editService.approveEditPageForOneUser();
  }

//  @GetMapping("/commit")
//  public String makeCommit(@RequestParam String login, @RequestParam Long id){
//    if(!validationService.validator(login, id)){
//      return "no access";
//    }
//    else {
//      return editService.makeCommit(id);
//    }
//  }
}
