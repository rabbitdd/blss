package main.controller;

import main.entity.Request;
import main.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/page")
public class PageController {

  @Autowired
  PageService pageService;

  @PostMapping("/add")
  public ResponseEntity<String> addPage(@RequestBody Request request) {
    return pageService.addPage(request);
  }

}
