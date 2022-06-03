package main.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.entity.User;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.rowset.spi.XmlReader;
import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorityGranter implements AuthorityGranter {

  private final XMLReader xml;

  @Override
  public Set<String> grant(Principal principal) {
    log.info(principal.getName());
    User user = xml.getToken(principal.getName());
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    Set<String> s = new HashSet<>();
    user.getRoles().forEach(role -> {
      s.add(role.getName());
    });
    return s;
  }
}
