package main.controller;

import main.bean.Status;
import main.entity.Change;
import main.entity.ChangeAnswer;
import main.entity.Request;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EditController {

  @Autowired ValidationService validationService;

  @Autowired SearchService searchService;

  @Autowired EditService editService;

  @Autowired PageService pageService;

  @Autowired ApproveService approveService;

  @PostMapping(path = "/edit")
  public ResponseEntity<?> editArticle(@RequestParam String login, @RequestBody Request request) {
    return editService.editWithApprove(login, request);
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

  @GetMapping("/getChanges")
  public ResponseEntity<List<ChangeAnswer>> getConfirmedChanges(
      @RequestParam String login, @RequestParam String name, @RequestParam Status status) {
    return editService.getChanges(login, name, status.toString());
  }
}
