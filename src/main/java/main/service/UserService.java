package main.service;

import main.entity.User;
import main.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long getUserId(String login) {
    Optional<User> userOptional = userRepository.getUserByLogin(login);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return user.getId();
    }
    return (long) -1;
  }

  public User getUserById(long id) {
    User user = userRepository.getUserById(id);
    return user;
  }

  public User getUserByLogin(String login) {
    Optional<User> userOptional = userRepository.getUserByLogin(login);
    if (!userOptional.isPresent()) {
      throw new UsernameNotFoundException("User not found");
    }
    return userOptional.get();
  }


}
