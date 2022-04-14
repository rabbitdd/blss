package main.service;

import main.entity.Page;
import main.repository.SearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchService {

  private final SearchRepository searchRepository;

  public SearchService(SearchRepository pageRepository) {
    this.searchRepository = pageRepository;
  }

  public Long getPageByName(String name) {
    Optional<Page> page = searchRepository.getPageByName(name);
    if (page.isPresent()) {
      Page p = page.get();
      return p.getId();
    }
    return (long) -1;
  }

  public Page getPageById(Long id) {
    return searchRepository.getPageById(id);
  }

  public List<Page> getAll() {
    return searchRepository.getAllBy();
  }
}
