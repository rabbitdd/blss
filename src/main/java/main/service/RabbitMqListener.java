package main.service;

import lombok.extern.slf4j.Slf4j;
import main.entity.Notification;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class RabbitMqListener {
  private AtomicBoolean isMessageConsumed;

  public RabbitMqListener() {
    this.isMessageConsumed = new AtomicBoolean(false);
  }

  public AtomicBoolean getIsMessageConsumed() {
    return isMessageConsumed;
  }

//  @RabbitListener(queues = "myQueue")
//  public void worker(Notification notification) throws InterruptedException {
//    log.info(notification.getUserSenderId().toString(), notification);
//    Thread.sleep(6000);
//  }
//
//  @RabbitListener(queues = "myQueue")
//  public void worker2(String message) throws InterruptedException {
//    log.info("worker2: {}", message);
//    Thread.sleep(6000);
//  }

  @RabbitListener(queues = "exampleQueue")
  public void listenMessage(byte[] in) throws IOException, ClassNotFoundException {
    log.info("Event consumed : " + deserialize(in).toString());
    isMessageConsumed.set(true);
  }

  public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
  }
}