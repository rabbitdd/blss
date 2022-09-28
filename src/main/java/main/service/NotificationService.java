package main.service;

import main.bean.Status;
import main.entity.*;
import main.exceptions.TransactionException;
import main.repository.AuthorRepository;
import main.repository.ChangeRepository;
import main.repository.NotificationRepository;
import main.repository.UserRepository;
import main.transaction.MKTransactionManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService implements UserFinder {

  private final AuthorRepository authorRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final ChangeRepository changeRepository;
  private final PageService pageService;
  private final TransactionTemplate transactionTemplate;

  public NotificationService(
          AuthorRepository authorRepository,
          NotificationRepository notificationRepository,
          UserRepository userRepository,
          ChangeRepository changeRepository,
          PageService pageService,
          PlatformTransactionManager transactionManager) {
    this.transactionTemplate = new TransactionTemplate(transactionManager);
    this.authorRepository = authorRepository;
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.changeRepository = changeRepository;
    this.pageService = pageService;
  }

  private boolean transactionForSendingConfirmations(Long senderUser, Page page, Long changeId){
    return (boolean) transactionTemplate.execute(new TransactionCallback(){

      @Override
      public Object doInTransaction(@NotNull TransactionStatus transactionStatus) {
        try{
          List<Long> authorId = findAllCoAuthorUsers(page.getId());
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
          return true;
        }
        catch (NullPointerException e){
          return false;
        }
      }
    });
  }

  public void sendConfirmationsToAllCoAuthors(Long senderUser, Page page, Long changeId) throws TransactionException {
    boolean flag = transactionForSendingConfirmations(senderUser, page, changeId);
    if(!flag){
      throw new TransactionException("Ошибка во время выполнения транзакции");
    }
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
              usersId.add(author.getUserId());
            });

    return usersId;
  }

  public ResponseEntity<?> getAllNotifications(String userLogin) {

    Optional<User> user = userRepository.getUserByLogin(userLogin);
    if (user.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK)
              .body(
                      this.notificationRepository.getNotificationsByUserId(user.get().getId()).stream()
                              .filter(
                                      notification ->
                                              notification.getStatus().equals(Status.NOT_CONFIRMED.toString()))
                              .collect(Collectors.toList()));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Пользователя с логином " + userLogin + " не существует !");
  }

  private String transactionToGetAllNotifications(Long changeId, Long userId, Long pageId, Notification currentNotification){
    return (String) transactionTemplate.execute(new TransactionCallback(){

      @Override
      public Object doInTransaction(@NotNull TransactionStatus transactionStatus) {
        try{
          notificationRepository.save(currentNotification);
          List<Notification> notificationList = notificationRepository.getAllByChangeId(changeId);
          boolean verdict =
                  notificationList.stream()
                          .allMatch(notification -> notification.getStatus().equals(Status.TRUE.toString()));
          boolean verdictFalse =
                  notificationList.stream()
                          .anyMatch(notification -> notification.getStatus().equals(Status.FALSE.toString()));

          if (verdict) {
            Optional<Change> changeById = changeRepository.getChangeById(changeId);
            Change change = changeById.get();
            change.setIs_confirmed(Status.TRUE.toString());
            changeRepository.save(change);

            if (authorRepository.findAuthorsByPageId(pageId).stream()
                    .noneMatch(author -> Objects.equals(author.getUserId(), userId))) {
              Author author = new Author();
              author.setUserId(userId);
              author.setPageId(pageId);
              authorRepository.save(author);
            }
            pageService.updatePage(change);
            return Status.TRUE.toString();
          }

          if (verdictFalse) {
            Optional<Change> changeById = changeRepository.getChangeById(changeId);
            Change change = changeById.get();
            change.setIs_confirmed(Status.FALSE.toString());
            changeRepository.save(change);
            return Status.FALSE.toString();
          }
          return Status.NOT_CONFIRMED.toString();
        }
        catch (NullPointerException | NoSuchElementException e){
          throw new TransactionException("Ошибка во время выполнения транзакции");
        }
      }
    });
  }

  public String getAllNotificationsByChangeId(Long changeId, Long userId, Long pageId, Notification currentNotification) throws TransactionException{
    return transactionToGetAllNotifications(changeId, userId, pageId, currentNotification);
  }
}
