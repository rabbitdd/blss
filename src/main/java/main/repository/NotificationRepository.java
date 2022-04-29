package main.repository;

import main.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> getNotificationsByUserIdAndUserSenderId(Long userId, Long userSenderId);

  List<Notification> getNotificationsByUserId(Long userId);
}
