package main.repository;

import main.entity.Notification;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
  void sendToMqtt(byte[] notification);
  void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
}
