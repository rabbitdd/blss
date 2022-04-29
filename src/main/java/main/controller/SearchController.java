package main.controller;

import main.entity.Page;
import main.entity.User;
import main.service.RecommendationService;
import main.service.SearchService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
  @Autowired SearchService searchService;

  @Autowired UserService userService;

  @GetMapping("/page")
  public ResponseEntity<String> getPage(@RequestParam String login, @RequestParam String name) {
    return searchService.getAnswer(userService.getUserByLogin(login), name);
  }
}
