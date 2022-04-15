package main.service;

import main.entity.*;
import main.repository.ChangeRepository;
import main.repository.CheckRepository;
import main.repository.SearchRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

  private final SearchRepository searchRepository;
  private final UserRepository userRepository;
  private final ChangeRepository changeRepository;
  private final CheckRepository checkRepository;

  public ValidationService(SearchRepository searchRepository, UserRepository userRepository,
                           CheckRepository checkRepository, ChangeRepository changeRepository) {
    this.searchRepository = searchRepository;
    this.userRepository = userRepository;
    this.checkRepository = checkRepository;
    this.changeRepository = changeRepository;
  }

  private boolean userExist(String login){
    if (!userRepository.existsUserByLogin(login)) {
      return false;
    }
    // check user has a valid role
    return true;
  }
  // TODO user has a valid role
  public boolean userHasValidRole(String login){
    if(userExist(login)){
      User user = userRepository.getUserByLogin(login).get();
      return user.getRole().equals("admin");
    }
    else {
      return false;
    }
  }
  // TODO page exist or else not exist create page
  private boolean pageExist(String page){
    return searchRepository.existsPageByName(page);
  }

  private boolean pageToUserVerification(Page page, String login){
    User user = userRepository.getUserByLogin(login).get();
    return pageExist(page.getName()) && page.getOwner().equals(user.getId());
  }
  // TODO check the comments

  public boolean validation(Request request) {
    Page page = request.getPage();
    String userLogin = request.getUserLogin();
    String comment = request.getComment();
    return userExist(userLogin) && pageToUserVerification(page, userLogin);
  }

  public long changeToAdminValidation(Verdict verdict){
    if(!(userHasValidRole(verdict.getLogin()) && pageExist(verdict.getPageName()))){
      return -1;
    }
    else{
      Page page = searchRepository.getPageByName(verdict.getPageName()).get();
      if(!changeRepository.existsByPageId(page.getId())){
        return -1;
      }
      Change change = changeRepository.getChangeByPageId(page.getId()).get();
      User user = userRepository.getUserByLogin(verdict.getLogin()).get();
      List<Check> checks = checkRepository.getAllByUserId(user.getId());
      long index = -1;
      for (Check check : checks) {
        if (check.getChange_id().equals(change.getId())) {
          index = check.getId();
          break;
        }
      }
      return index;
    }
  }
}
