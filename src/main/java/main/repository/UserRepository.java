package main.repository;

import main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

  User getUserById(Long id);

  Optional<User> getUserByLogin(String login);

  String existsUserByLogin(String login);
}
