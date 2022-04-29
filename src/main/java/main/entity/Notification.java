package main.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "notification")
public class Notification {
  @Id
  @Column(name = "notification_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "user_sender_id")
  private Long userSenderId;

  @Column(name = "status")
  private Boolean status;
}
