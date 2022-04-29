package main.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.entity.User;
import main.entity.Verdict;
import main.service.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class SecurityController {

  private final CustomerUserDetailService customerUserDetailService;

  @PostMapping("/auth")
  public ResponseEntity<String> getLoginPage(@RequestBody AuthUser user) {
    System.out.println(user.toString());
    System.out.println("****");
    UserDetails securityUser = customerUserDetailService.loadUserByUsername(user.getLogin());
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    if (encoder.matches(user.getPassword(), securityUser.getPassword())) {
      return new ResponseEntity<>(securityUser.getPassword(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Неправильный логин или пароль", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/signUp")
  public ResponseEntity<String> signUp(@RequestBody AuthUser user) {
    System.out.println(user.toString());
    System.out.println("sdsdsd");
    String hashPassword = new BCryptPasswordEncoder(12).encode(user.getPassword());
    user.setPassword(hashPassword);
    User u = new User();
    u.setLogin(user.getLogin());
    u.setPassword(user.getPassword());
    return customerUserDetailService.addUser(u);
  }

  @GetMapping("/auth")
  public String getAuth() {
    return "auth";
  }

  @Data
  @NoArgsConstructor
  public static class AuthUser {
    String login;
    String password;
  }
}
