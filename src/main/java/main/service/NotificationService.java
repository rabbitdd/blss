package main.service;

import main.entity.Author;
import main.entity.Notification;
import main.entity.Page;
import main.entity.User;
import main.repository.AuthorRepository;
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

  public NotificationService(
      AuthorRepository authorRepository,
      NotificationRepository notificationRepository,
      UserRepository userRepository) {
    this.authorRepository = authorRepository;
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
  }

  public void sendConfirmationsToAllCoAuthors(Long senderUser, Page page) {
    List<Long> authorId = this.findAllCoAuthorUsers(page.getId());
    authorId.forEach(
        id -> {
          Notification notification = new Notification();
          notification.setUserId(id);
          notification.setUserSenderId(senderUser);
          notification.setStatus(false);
          notificationRepository.save(notification);
        });
  }

  public void acknowledgeNotification(Notification notification) {
    notification.setStatus(true);
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
              this.notificationRepository.getNotificationsByUserIdAndStatus(
                  user.get().getId(), false));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователя с логином " + userLogin + " не существует !");
  }
}
