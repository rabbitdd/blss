package main.controller;

import lombok.AllArgsConstructor;
import main.entity.Request;
import main.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class PageController {


  private final PageService pageService;

  @PostMapping("/add")
  public ResponseEntity<String> addPage(@RequestParam String login, @RequestBody Request request) {
    return pageService.addPage(login, request);
  }

}
