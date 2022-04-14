package main.controller;

import main.entity.Request;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("wiki")
public class EditController {

  @PostMapping("/edit")
  public void editArticle(@RequestBody Request request) {}
}
