package main.service;

import main.entity.Author;
import main.entity.Notification;
import main.entity.Page;
import main.repository.AuthorRepository;
import main.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService implements UserFinder {

  private final AuthorRepository authorRepository;
  private final NotificationRepository notificationRepository;

  public NotificationService(
      AuthorRepository authorRepository, NotificationRepository notificationRepository) {
    this.authorRepository = authorRepository;
    this.notificationRepository = notificationRepository;
  }

  public boolean sendConfirmationsToAllCoAuthors(Long senderUser, Page page) {
    List<Long> authorId = this.findAllCoAuthorUsers(page.getId());
    authorId.forEach(
        id -> {
          Notification notification = new Notification();
          notification.setUserId(id);
          notification.setUserSenderId(senderUser);
          notification.setStatus(false);
          notificationRepository.save(notification);
        });
    return true;
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
}
