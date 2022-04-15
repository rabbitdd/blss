package main.service;

import main.entity.*;
import main.repository.ChangeRepository;
import main.repository.CheckRepository;
import main.repository.SearchRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApproveService {

  // TODO make approve algorithms

  private final SearchRepository searchRepository;
  private final UserRepository userRepository;
  private final CheckRepository checkRepository;
  private final ChangeRepository changeRepository;

  public ApproveService(SearchRepository searchRepository, UserRepository userRepository,
                        CheckRepository checkRepository, ChangeRepository changeRepository) {
    this.searchRepository = searchRepository;
    this.userRepository = userRepository;
    this.checkRepository = checkRepository;
    this.changeRepository = changeRepository;
  }

  public boolean approveEditArticle() {

    return true;
  }

  public boolean approve(Verdict verdict, long index){
    checkRepository.setUserInfoById(verdict.is_confirmed(), verdict.getComment(), index);
    return true;
  }

  public String getToApprove(String login){
    StringBuilder string = new StringBuilder();
    User user = userRepository.getUserByLogin(login).get();
    List<Check> checks = checkRepository.getAllByUserId(user.getId());
    for (Check check : checks) {
      if(check.getIs_confirmed() == null) {
        Change change = changeRepository.getById(check.getChangeId());
        System.out.println(change.getId());
        Page page = searchRepository.getPageById(change.getPageId());
        string.append(page.getName()).append("\n");
      }
    }
    return string.toString();
  }

}
