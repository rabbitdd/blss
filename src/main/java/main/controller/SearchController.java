package main.controller;

import main.entity.Page;
import main.entity.User;
import main.service.RecommendationService;
import main.service.SearchService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
  @Autowired SearchService searchService;

  @Autowired UserService userService;

  @Autowired RecommendationService recommendationService;

  @GetMapping("/page")
  public String getPage(@RequestParam String login, @RequestParam String name) {
    // todo убрать в сервис
    long id = searchService.getPageByName(name);
    Page page = searchService.getPageById(id);
    User user = userService.getUserByLogin(login);
    if (id == -1) {
      List<Page> pageList = searchService.getAll();
      return recommendationService.getRecommendations(pageList, user, name);
    }
    return recommendationService.getAnswer(page, user);
  }
}
