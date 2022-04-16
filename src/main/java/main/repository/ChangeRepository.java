package main.repository;

import main.entity.Change;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ChangeRepository extends JpaRepository<Change, Long> {
  Optional<Change> getChangeByPageId(Long pageId);

  Boolean existsByPageId(Long pageId);
}
