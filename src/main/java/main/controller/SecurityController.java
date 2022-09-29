package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.entity.Role;
import main.entity.User;
import main.service.UserServiceImpl;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SecurityController {

  private final UserServiceImpl userService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/signUp")
  @Transactional
  public ResponseEntity<String> signUp(@RequestBody AuthRequest request) {
    log.info("SignUp user with login {}", request.getLogin());
    if (userService.existsUserByLogin(request.getLogin())) {
      return new ResponseEntity<>("Пользователь с таким именем существует !", HttpStatus.CONFLICT);
    }
    List<Role> roles = new ArrayList<>();
    roles.add(userService.getRoleByName("ROLE_READER"));
    userService.saveUser(new User(null, request.getLogin(), request.getPassword(), roles));
    return new ResponseEntity<>("Успешно зарегестрированны !", HttpStatus.CREATED);
  }

  @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  @Transactional
  public ResponseEntity<String> signIn(@RequestParam String login, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
    log.info("SignIn user with login {}", login);

    return new ResponseEntity<String>("Thank you for submitting feedback", HttpStatus.OK);
  }

  @PostMapping(path = "/saveUserInfo")
  @Transactional
  public void saveUserInfo() throws JAXBException, XMLStreamException, FileNotFoundException {
    log.info("save");
    userService.saveUsersInfo();
    // return new ResponseEntity<String>("Thank you for submitting feedback", HttpStatus.OK);
  }

//  @PostMapping("/auth")
//  public HttpServletResponse getLoginPage(@RequestParam String  HttpServletRequest request, HttpServletResponse response) {
//    return response;
//  }

//  @PostMapping("/signUp")
//  public ResponseEntity<String> signUp(@RequestBody User user) {

//    System.out.println(user.toString());
//    System.out.println("sdsdsd");
//    String hashPassword = new BCryptPasswordEncoder(12).encode(user.getPassword());
//    user.setPassword(hashPassword);
//    User u = new User();
//    u.setLogin(user.getLogin());
//    u.setPassword(user.getPassword());
//    return customerUserDetailService.addUser(u);
  //}

  @GetMapping("/auth")
  public String getAuth() {
    return "auth";
  }


  @Data
  @NoArgsConstructor
  public static class AuthRequest {
    private String login;
    private String password;
  }
}
