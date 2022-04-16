package main.service;

import main.entity.Page;
import main.entity.Request;
import main.repository.PageRepository;
import main.repository.ValidationRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class PageService {

  private final PageRepository pageRepository;

  public PageService(PageRepository pageRepository) {
    this.pageRepository = pageRepository;
  }

  public String addPage(Request request) {
    Page page = request.getPage();
    if (this.validationRequestPage(page)) {
      pageRepository.save(page);
      return "page added";
    }
    return "page not added";
  }

  public boolean validationRequestPage(Page page) {
    return Stream.of(page.getId(), page.getOwner(), page.getName(), page.getRole(), page.getText())
            .noneMatch(Objects::isNull);
  }
}
