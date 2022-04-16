package main.repository;

import main.entity.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public interface CheckRepository  extends JpaRepository<Check, Long>{
   List<Check> getAllByUserId(Long userId);

   @Transactional
   @Modifying
   @Query("update checks c set c.is_confirmed = ?1, c.comment = ?2 where c.id = ?3")
   void setUserInfoById(Boolean flag, String comment, Long id);

   List<Check> getAllByChangeId(Long changeId);

   @Transactional
   void deleteAllByChangeId(Long id);
}
