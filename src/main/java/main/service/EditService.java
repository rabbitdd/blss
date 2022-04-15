package main.service;

import main.entity.Change;
import main.entity.Check;
import main.entity.User;
import main.repository.ChangeRepository;
import main.repository.CheckRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditService {

    private final ChangeRepository changeRepository;
    private final UserRepository userRepository;
    private final CheckRepository checkRepository;

    public EditService(ChangeRepository changeRepository, UserRepository userRepository,
                       CheckRepository checkRepository) {
        this.changeRepository = changeRepository;
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
    }

    public boolean addChange(Long id, String change){
        Optional<Change> check = changeRepository.getChangeByPageId(id);
        if(check.isPresent()){
            return false;
        }
        Change change1 = new Change();
        change1.setText(change);
        change1.setPageId(id);
        changeRepository.save(change1);
        return addChecks(id);
    }

    private boolean addChecks(Long id){
        List<User> users = userRepository.getAllByRole("admin");
        if(users.size() >= 3) {
            Change change = changeRepository.getChangeByPageId(id).get();
            User user;
            Check check;
            long random;
            for (int i = 0; i < 3; i++) {
                random = Math.round(Math.random() * (users.size() - 1));
                user = users.get((int) random);
                check = new Check();
                check.setChange_id(change.getId());
                check.setUserId(user.getId());
                checkRepository.save(check);
                users.remove((int) random);
            }
            return true;
        }
        else{
            return false;
        }
    }

}
