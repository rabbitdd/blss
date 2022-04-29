package main.controller;

import lombok.AllArgsConstructor;
import main.service.SearchService;
import main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SearchController {
  private final SearchService searchService;

  private final UserService userService;

  @GetMapping("/page")
  public ResponseEntity<String> getPage(@RequestParam String login, @RequestParam String name) {
    return searchService.getAnswer(userService.getUserByLogin(login), name);
  }
}
