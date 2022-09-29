package main.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
public class QueueBean {

//  private final ConnectionFactory connectionFactory;
//
//  @Bean
//  public AmqpAdmin amqpAdmin() {
//    return new RabbitAdmin(connectionFactory);
//  }

}
