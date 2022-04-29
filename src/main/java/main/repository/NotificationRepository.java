package main.repository;

import main.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Optional<Notification> getTopByUserIdAndUserSenderIdAndPageIdAndStatusOrderByIdDesc(Long userId, Long userSenderId, Long pageId, Boolean status);

  List<Notification> getNotificationsByUserIdAndStatus(Long userId, Boolean status);
}
