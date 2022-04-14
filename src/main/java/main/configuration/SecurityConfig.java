package main.configuration;

import main.service.CustomerUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomerUserDetailService service;

  public SecurityConfig(CustomerUserDetailService service) {
    this.service = service;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors();
    String pointAuth = "/auth";
    String pointSignUp = "/signUp";
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(pointAuth)
        .permitAll()
        .antMatchers(pointSignUp)
        .permitAll()
        .and()
        .httpBasic()
        .and()
        .sessionManagement()
        .disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(service);
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
