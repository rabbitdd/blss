package main.configuration;

import lombok.RequiredArgsConstructor;
import main.filter.CustomAuthFilter;
import main.filter.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  private static final String[] AUTH_WHITELIST = {
    "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**"
  };

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(AUTH_WHITELIST);
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManager());
    customAuthFilter.setFilterProcessesUrl("/home");
    customAuthFilter.setFilterProcessesUrl("/login");
    httpSecurity.csrf().disable();
    httpSecurity
        .authorizeRequests()
        .antMatchers("/swagger-ui/**", "/token/refresh", "/signIn", "/signUp", "/saveUserInfo")
        .permitAll();
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.authorizeRequests().antMatchers(GET, "/page").hasAnyAuthority("ROLE_READER");
    httpSecurity.authorizeRequests().antMatchers(GET, "/users").hasAnyAuthority("ROLE_ADMIN");
    httpSecurity.authorizeRequests().antMatchers(POST, "/user/**").hasAnyAuthority("ROLE_ADMIN");
    httpSecurity.authorizeRequests().antMatchers(POST, "/user/**").hasAnyAuthority("ROLE_ADMIN");
    httpSecurity
        .authorizeRequests()
        .antMatchers(POST, "/edit", "/verdict")
        .hasAnyAuthority("ROLE_EDITOR");
    httpSecurity
        .authorizeRequests()
        .antMatchers(GET, "/getChanges", "/getAllApprovePages", "/getAllNotification")
        .hasAnyAuthority("ROLE_EDITOR");
    httpSecurity.authorizeRequests().antMatchers(POST, "/add").hasAnyAuthority("ROLE_WRITER");
    httpSecurity.authorizeRequests().anyRequest().authenticated();
    httpSecurity.addFilter(customAuthFilter);
    httpSecurity.addFilterBefore(
        new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
