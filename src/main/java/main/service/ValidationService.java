package main.service;

import main.entity.Page;
import main.entity.Request;
import main.repository.SearchRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private final SearchRepository searchRepository;
  private final UserRepository userRepository;

  public ValidationService(SearchRepository searchRepository, UserRepository userRepository) {
    this.searchRepository = searchRepository;
    this.userRepository = userRepository;
  }

  // TODO user exist
  // TODO user has a valid role
  // TODO page exist or else not exist create page
  // TODO check the comments

  public boolean validation(Request request) {
    Page page = request.getPage();
    String userLogin = request.getUserLogin();
    String comment = request.getComment();

    return true;
  }
}
