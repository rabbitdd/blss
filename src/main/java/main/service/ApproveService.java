package main.service;

import main.bean.Status;
import main.entity.Notification;
import main.entity.Page;
import main.entity.User;
import main.entity.Verdict;
import main.repository.NotificationRepository;
import main.repository.PageRepository;
import main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApproveService {

  private final NotificationService notificationService;

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final PageRepository pageRepository;

  public ApproveService(
          NotificationService notificationService, UserRepository userRepository,
          NotificationRepository notificationRepository,
          PageRepository pageRepository) {
    this.notificationService = notificationService;

    this.userRepository = userRepository;
    this.notificationRepository = notificationRepository;
    this.pageRepository = pageRepository;
  }

  public Verdict approve(Verdict verdict) {
    Optional<User> firstUser = userRepository.getUserByLogin(verdict.getUserLogin());
    Optional<User> secondUser = userRepository.getUserByLogin(verdict.getWhoToConfirm());
    Optional<Page> page = pageRepository.getPageByName(verdict.getPageName());

    if (firstUser.isPresent() && secondUser.isPresent() && page.isPresent()) {
      Optional<Notification> notification =
          notificationRepository.getTopByUserIdAndUserSenderIdAndPageIdAndStatusOrderByIdDesc(
              firstUser.get().getId(),
              secondUser.get().getId(),
              page.get().getId(),
              Status.NOT_CONFIRMED.toString());
      if (notification.isPresent()) {
        Notification currentNotification = notification.get();
        String oldStatus = currentNotification.getStatus();
        String newStatus = validateVerdictStatus(verdict.getIs_confirmed());
        currentNotification.setStatus(newStatus);
        notificationRepository.save(currentNotification);
        verdict.setResponseVerdictAns("???????????? ?????????????????? ?? " + oldStatus + " ???? " + newStatus);

        checkApproveStatus(
            currentNotification.getChangeId(), secondUser.get().getId(), page.get().getId());
        return verdict;
      }
      verdict.setResponseVerdictAns("?????? ???????? ?????????????????????????? ?????? ?????????????????????????? !");
      return verdict;
    }

    verdict.setResponseVerdictAns("???????????????????????? ?? ???????????? ???????????????? ???? ?????????????? !");
    return verdict;
  }

  private String validateVerdictStatus(String status){
    if ("TRUE".equals(status) || "FALSE".equals(status))
      return status;
    return "NOT_CONFIRMED";
  }

  private void checkApproveStatus(Long changeId, Long userId, Long pageId) {
    notificationService.getAllNotificationsByChangeId(changeId, userId, pageId);
  }

  public ResponseEntity<?> getApprovePages(String login) {
    Optional<User> user = userRepository.getUserByLogin(login);
    if (user.isPresent()) {
      List<Page> pagesWithStatus = new ArrayList<>();
      List<Notification> notificationList =
          notificationRepository.getNotificationsByUserId(user.get().getId());
      notificationList.forEach(
          notification -> {
            Optional<Page> page = pageRepository.getPageById(notification.getPageId());
            page.ifPresent(pagesWithStatus::add);
          });
      return ResponseEntity.status(HttpStatus.OK).body(pagesWithStatus);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("???????????????????????? ?? ?????????????? " + login + " ???? ???????????????????? !");
  }
}
