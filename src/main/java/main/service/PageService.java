package main.service;

import main.entity.Author;
import main.entity.Page;
import main.entity.Request;
import main.entity.User;
import main.repository.AuthorRepository;
import main.repository.PageRepository;
import main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PageService {

  private final PageRepository pageRepository;
  private final AuthorRepository authorRepository;
  private final UserRepository userRepository;

  public PageService(
      PageRepository pageRepository,
      AuthorRepository authorRepository,
      UserRepository userRepository) {
    this.pageRepository = pageRepository;
    this.authorRepository = authorRepository;
    this.userRepository = userRepository;
  }

  public ResponseEntity<String> addPage(Request request) {
    Page page = request.getPage();
    if (validationRequestPage(page)) {
      Optional<User> user = userRepository.getUserByLogin(request.getUserLogin());
      if (pageRepository.existsPageByName(page.getName()))
        return new ResponseEntity<>(
            "Страница с таким именем уже существует !", HttpStatus.BAD_REQUEST);
      // todo если статья с таким именем уже есть, то отправить сообщение об ошбике
      if (user.isPresent()) {
        Author author = new Author();
        author.setUserId(user.get().getId());
        author.setPageId(page.getId());
        authorRepository.save(author);
        pageRepository.save(page);
        return new ResponseEntity<>(
            "Страница создана пользователем c логином " + user.get().getLogin(),
            HttpStatus.CREATED);
      }
      return new ResponseEntity<>(
          "Невозможно создать страницу, пользователя с логином "
              + request.getUserLogin()
              + " не существует !",
          HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("Страница не прошла валидацию !", HttpStatus.BAD_REQUEST);
  }

  private boolean validationRequestPage(Page page) {
    return Stream.of(page.getId(), page.getOwner(), page.getName(), page.getRole(), page.getText())
        .noneMatch(Objects::isNull);
  }
}
