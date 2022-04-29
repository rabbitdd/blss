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

  public ApproveService(
      SearchRepository searchRepository,
      UserRepository userRepository,
      CheckRepository checkRepository,
      ChangeRepository changeRepository) {
    this.searchRepository = searchRepository;
    this.userRepository = userRepository;
    this.checkRepository = checkRepository;
    this.changeRepository = changeRepository;
  }

  public boolean approveEditArticle() {

    return true;
  }

  public boolean approve(Verdict verdict, String nameOfArticle) {

    // todo подтвердить статью с именем
    return true;
  }

  public String getToApprove(String login) {
    // todo возвращать соавтору список статей которые он должен подтвердить
    // todo если не соавтор => роль админ на подтверждение статей
    return null;
  }
}
