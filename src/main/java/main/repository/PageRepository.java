package main.repository;

import main.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public interface PageRepository extends JpaRepository<Page, Long> {
    Page getPageById(Long id);
    Optional<Page> getPageByName(String name);
    ArrayList<Page> getAllBy();
}
