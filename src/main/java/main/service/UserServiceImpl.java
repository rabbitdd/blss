package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.entity.Role;
import main.entity.User;
import main.repository.RoleRepository;
import main.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByLogin(username);
    if (user == null) {
      log.error("User not found");
      throw new UsernameNotFoundException("User not found");
    } else {
      log.info("User {} found", username);
    }
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles().forEach(role -> {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    });
    return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
  }

  @Override
  public User getUserById(Long id) {
    log.info("Fetching user by Id");
    return userRepository.getUserById(id);
  }

  @Override
  public User saveUser(User user) {
    log.info("Saving new user {} to database", user.getLogin());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public Long getUserIdByLogin(String login) {
    log.info("Fetching user id for user {}", login);
    Optional<User> userOptional = userRepository.getUserByLogin(login);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return user.getId();
    }
    return (long) -1;
  }

  @Override
  public User getUserByLogin(String login) {
    log.info("Fetching user {}", login);
    Optional<User> userOptional = userRepository.getUserByLogin(login);
    if (!userOptional.isPresent()) {
      throw new UsernameNotFoundException("User not found");
    }
    return userOptional.get();
  }

  @Override
  public void saveRole(Role role) {
    log.info("Saving new role {} to database", role.getName());
    roleRepository.save(role);
  }

  @Override
  public List<User> findAll() {
    log.info("Fetching all users");
    return userRepository.findAll();
  }

  @Override
  public Boolean existsUserByLogin(String login) {
    return userRepository.existsUserByLogin(login);
  }

  @Override
  public void addRoleToUser(String roleName, String login) {
    log.info("Adding role {} to user {}", roleName, login);
    User user = userRepository.findUserByLogin(login);
    Role role = roleRepository.getRoleByName(roleName);
    user.getRoles().add(role);
  }

  @Override
  public Role getRoleByName(String name) {
    return roleRepository.getRoleByName(name);
  }

  public void saveUsersInfo() throws JAXBException, FileNotFoundException, XMLStreamException {
//    XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream("C:\\Users\\Никита\\Desktop\\itmo\\6 семестр\\БЛПС\\BLPS1\\src\\main\\resources\\info.xml"), StandardCharsets.UTF_8.name());
//    writer.writeStartDocument();
//    writer.writeStartElement("YOUR_ROOT_ELM");
//    JAXBContext contextObj = JAXBContext.newInstance(User.class);
//    Marshaller marshallerObj = contextObj.createMarshaller();
//    marshallerObj.setProperty(Marshaller.JAXB_FRAGMENT, true);
//    PartialUnmarshaler existing =


//    File file = new File("C:\\Users\\Никита\\Desktop\\itmo\\6 семестр\\БЛПС\\BLPS1\\src\\main\\resources\\info.xml");
//    List<User> users = findAll();
//    JAXBContext contextObj = JAXBContext.newInstance(User.class);
//    Marshaller marshallerObj = contextObj.createMarshaller();
//    users.forEach(user -> {
//      try {
//        log.info("write ....");
//        marshallerObj.marshal(user, file);
//      } catch (JAXBException e) {
//        e.printStackTrace();
//      }
//    });
  }
}
