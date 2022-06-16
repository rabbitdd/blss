package main.security;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import main.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.Name;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.rowset.spi.XmlReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class JAASLoginModule implements LoginModule {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private XMLReader xml;
  private Subject subject;
  private CallbackHandler callbackHandler;

  private String username;
  private User user;
  private boolean loginSucceeded = false;

  @Override
  public void initialize(
      Subject subject,
      CallbackHandler callbackHandler,
      Map<String, ?> sharedState,
      Map<String, ?> options) {
    log.info("initialize");

    this.subject = subject;
    this.callbackHandler = callbackHandler;
    this.xml = (XMLReader) options.get("xmlReader");
  }

  @Override
  public boolean login() {
    log.info("login");
    NameCallback nameCallback = new NameCallback("login");
    log.info("callback first");
    PasswordCallback passwordCallback = new PasswordCallback("password", false);
    log.info("callback second");
    try {
      callbackHandler.handle(new Callback[] {nameCallback, passwordCallback});
      log.info("callback handle");
      username = nameCallback.getName();
      log.info(username);
      String password = String.valueOf(passwordCallback.getPassword());
      log.info(password);
      log.info(xml.toString() + " some looooooogs");
      if (xml == null)
        log.info("xml is null");
      User user = xml.getToken(username);
      if (user == null)
        log.info("user is null");
      log.info(user.toString() + "some log neeeeeeeeed");
      this.user = user;
//      String password = String.valueOf(passwordCallback.getPassword());
//      log.info(password);
      String currentPassword = user.getPassword();
      log.info(currentPassword);
      log.info(user.toString());
      loginSucceeded = passwordEncoder.matches(password, currentPassword);
      log.info(String.valueOf(loginSucceeded));
    } catch (IOException | UnsupportedCallbackException e) {
      e.printStackTrace();
    }
    return loginSucceeded;
  }

  @Override
  public boolean commit() throws LoginException {
    log.info("commit");
    if(!loginSucceeded){
      return false;
    }
    if(username == null){
      throw new LoginException("Username is null");
    }
    Principal principal = new UserPrincipal(username);
    subject.getPrincipals().add(principal);
    return true;
  }

  @Override
  public boolean abort() throws LoginException {
    log.info("abort");
    return false;
  }

  @Override
  public boolean logout() throws LoginException {
    log.info("logout");
    return false;
  }
}
