package main.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
public class JavaEEConfiguration {
  @Bean(name = "transactionManager")
  public JtaTransactionManager provideTransactionManager(){
    return new JtaTransactionManager();
  }
}
