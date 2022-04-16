package main.controller;

import main.entity.Page;
import main.entity.Request;
import main.service.ApproveService;
import main.service.EditService;
import main.service.SearchService;
import main.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("wiki")
public class EditController {

  @Autowired
  ValidationService validationService;

  @Autowired
  SearchService searchService;

  @Autowired
  EditService editService;

  @Autowired
  ApproveService approveService;

  @PostMapping("/edit")
  public String editArticle(@RequestBody Request request) {
    if(validationService.validation(request)){
      Long page = searchService.getPageByName(request.getPage().getName());
      if(editService.addChange(page, request.getComment())){
        return "Successfully added, expect a verdict";
      }
      else{
        return "one version is already under review";
      }
    }
    else{
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
  @GetMapping("/commit")
  public String makeCommit(@RequestParam String login, @RequestParam Long id){
    if(!validationService.validator(login, id)){
      return "no access";
    }
    else {
      return editService.makeCommit(id);


    }
  }
}
