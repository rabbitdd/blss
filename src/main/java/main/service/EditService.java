package main.service;

import main.entity.*;
import main.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditService {

  private final ChangeRepository changeRepository;
  private final UserRepository userRepository;
  private final CheckRepository checkRepository;
  private final AuthorRepository authorRepository;
  private final NotificationRepository notificationRepository;
  private final PageRepository pageRepository;
  private final NotificationService notificationService;
  private final ValidationService validationService;

  public EditService(
      ChangeRepository changeRepository,
      UserRepository userRepository,
      CheckRepository checkRepository,
      AuthorRepository authorRepository,
      NotificationRepository notificationRepository,
      PageRepository pageRepository,
      NotificationService notificationService,
      ValidationService validationService) {
    this.changeRepository = changeRepository;
    this.userRepository = userRepository;
    this.checkRepository = checkRepository;
    this.authorRepository = authorRepository;
    this.notificationRepository = notificationRepository;
    this.pageRepository = pageRepository;
    this.notificationService = notificationService;
    this.validationService = validationService;
  }

  public boolean addChange(Long id, String change) {
    Optional<Change> check = changeRepository.getChangeByPageId(id);
    if (check.isPresent() && check.get().getIs_confirmed() == null) {
      return false;
    }
    Change change1 = new Change();
    change1.setText(change);
    change1.setPageId(id);
    changeRepository.save(change1);
    return addChecks(id);
  }

  public boolean editWithApprove(Request request) {
    if (validationService.validationRequestPage(request.getPage())) {
      Optional<User> user = userRepository.getUserByLogin(request.getUserLogin());
      user.ifPresent(
          value ->
              notificationService.sendConfirmationsToAllCoAuthors(
                  value.getId(), request.getPage()));
      return true;
    }
    return false;
  }

  public boolean checkAllNotification(List<Notification> notifications) {
    return notifications.stream().allMatch(Notification::getStatus);
  }

  private boolean addChecks(Long id) {
    List<User> users = userRepository.getAllByRole("admin");
    if (users.size() >= 3) {
      Change change = changeRepository.getChangeByPageId(id).get();
      User user;
      Check check;
      long random;
      for (int i = 0; i < 3; i++) {
        random = Math.round(Math.random() * (users.size() - 1));
        user = users.get((int) random);
        check = new Check();
        check.setChangeId(change.getId());
        check.setUserId(user.getId());
        checkRepository.save(check);
        users.remove((int) random);
      }
      return true;
    } else {
      return false;
    }
  }

  public String getStatus(Long id) {
    Optional<Change> change = changeRepository.getChangeByPageId(id);
    if (!change.isPresent()) {
      return "noting to check";
    } else {
      updateStatus(change.get());
      Change ch = changeRepository.getChangeByPageId(id).get();
      if (ch.getIs_confirmed() == null) {
        return "still under review";
      } else {
        if (ch.getIs_confirmed()) {
          return "everything is fine";
        } else {
          return "not accepted";
        }
      }
    }
  }

  private void updateStatus(Change change) {
    List<Check> checks = checkRepository.getAllByChangeId(change.getId());
    int sum = 0;
    boolean flag = true;
    for (Check check : checks) {
      if (check.getIs_confirmed() == null) {
        sum++;
      } else {
        if (!check.getIs_confirmed()) {
          flag = false;
        }
      }
    }
    if (sum == 0) {
      changeRepository.setUserInfoById(flag, change.getId());
    }
  }

  // todo rewrite logic of method

  public boolean approveEditPageForOneUser(Long userId, Long senderId) {
    List<Notification> notifications =
        notificationRepository.getNotificationsByUserIdAndUserSenderId(userId, senderId);
    notifications.forEach(
        notification -> {
          notification.setStatus(true);
          notificationRepository.save(notification);
        });
    return true;
  }
}
