package main.service;

import main.bean.Status;
import main.entity.*;
import main.repository.AuthorRepository;
import main.repository.ChangeRepository;
import main.repository.NotificationRepository;
import main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService implements UserFinder {

  private final AuthorRepository authorRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final ChangeRepository changeRepository;
  private final PageService pageService;

  public NotificationService(
          AuthorRepository authorRepository,
          NotificationRepository notificationRepository,
          UserRepository userRepository, ChangeRepository changeRepository, PageService pageService) {
    this.authorRepository = authorRepository;
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.changeRepository = changeRepository;
    this.pageService = pageService;
  }

  public void sendConfirmationsToAllCoAuthors(Long senderUser, Page page, Long changeId) {
    List<Long> authorId = this.findAllCoAuthorUsers(page.getId());
    authorId.forEach(
        id -> {
          Notification notification = new Notification();
          notification.setUserId(id);
          notification.setUserSenderId(senderUser);
          notification.setStatus(Status.NOT_CONFIRMED.toString());
          notification.setPageId(page.getId());
          notification.setChangeId(changeId);
          notificationRepository.save(notification);
        });
  }

  public void acknowledgeNotification(Notification notification) {
    notification.setStatus(Status.TRUE.toString());
    notificationRepository.save(notification);
  }

  @Override
  public List<Long> findAllCoAuthorUsers(Long pageId) {
    ArrayList<Author> authors = new ArrayList<>(authorRepository.findAuthorsByPageId(pageId));
    List<Long> usersId = new ArrayList<>();
    authors.forEach(
        author -> {
          usersId.add(author.getId());
        });

    return usersId;
  }

  public ResponseEntity<?> getAllNotifications(String userLogin) {

    Optional<User> user = userRepository.getUserByLogin(userLogin);
    if (user.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              this.notificationRepository.getNotificationsByUserId(
                  user.get().getId()));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователя с логином " + userLogin + " не существует !");
  }

  public String getAllNotificationsByChangeId(Long changeId, Long userId, Long pageId) {

    List<Notification> notificationList = notificationRepository.getAllByChangeId(changeId);
    boolean verdict = notificationList.stream().allMatch(notification -> notification.getStatus().equals(Status.TRUE.toString()));
    boolean verdictFalse = notificationList.stream().anyMatch(notification -> notification.getStatus().equals(Status.FALSE.toString()));

    if (verdict) {
      Optional<Change> changeById = changeRepository.getChangeById(changeId);
      Change change = changeById.get();
      change.setIs_confirmed(Status.TRUE.toString());
      changeRepository.save(change);

      if (authorRepository.findAuthorsByPageId(pageId).stream().noneMatch(author -> Objects.equals(author.getUserId(), userId))) {
        Author author = new Author();
        author.setUserId(userId);
        author.setPageId(pageId);
        authorRepository.save(author);
      }
      pageService.updatePage(change);


      return Status.TRUE.toString();
    }

    if (verdictFalse)
      return Status.FALSE.toString();
    return Status.NOT_CONFIRMED.toString();
  }
}
