package main.service;

import main.entity.Author;
import main.entity.Page;
import main.entity.Request;
import main.entity.User;
import main.repository.AuthorRepository;
import main.repository.PageRepository;
import main.repository.UserRepository;
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

  public String addPage(Request request) {
    Page page = request.getPage();
    if (this.validationRequestPage(page)) {
      Optional<User> user = userRepository.getUserByLogin(request.getUserLogin());
      pageRepository.save(page);
      // todo если статья с таким именем уже есть, то отправить сообщение об ошбике
      if (user.isPresent()) {
        Author author = new Author();
        author.setUserId(user.get().getId());
        author.setPageId(page.getId());
        authorRepository.save(author);
      }
      return "page added";
    }
    return "page not added";
  }

  public boolean validationRequestPage(Page page) {
    return Stream.of(page.getId(), page.getOwner(), page.getName(), page.getRole(), page.getText())
        .noneMatch(Objects::isNull);
  }
}
