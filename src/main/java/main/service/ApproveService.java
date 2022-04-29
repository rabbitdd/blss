package main.service;

import main.bean.PageWithStatus;
import main.entity.*;
import main.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApproveService {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final PageRepository pageRepository;

  public ApproveService(
      UserRepository userRepository,
      NotificationRepository notificationRepository,
      PageRepository pageRepository) {

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
              firstUser.get().getId(), secondUser.get().getId(), page.get().getId(), false);
      if (notification.isPresent()) {
        Notification currentNotification = notification.get();
        currentNotification.setStatus(true);
        notificationRepository.save(currentNotification);
        verdict.setResponseVerdictAns("Подтверждение прошло успешно");
        return verdict;
      }
      verdict.setResponseVerdictAns("Для этих пользователей нет подтверждений !");
      return verdict;
    }

    verdict.setResponseVerdictAns("Пользователи с такими логинами не найдены !");
    return verdict;
  }

  public ResponseEntity<?> getApprovePages(String login) {
    Optional<User> user = userRepository.getUserByLogin(login);
    if (user.isPresent()) {
      List<Page> pagesWithStatus = new ArrayList<>();
      List<Notification> notificationList =
          notificationRepository.getNotificationsByUserIdAndStatus(user.get().getId(), false);
      notificationList.forEach(
          notification -> {
            Optional<Page> page = pageRepository.getPageById(notification.getPageId());
            page.ifPresent(pagesWithStatus::add);
          });
      return ResponseEntity.status(HttpStatus.OK).body(pagesWithStatus);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Пользователя с логином " + login + " не существует !");
  }
}
