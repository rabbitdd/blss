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
        if(check.isPresent() && check.get().getIs_confirmed() == null){
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
                check.setChangeId(change.getId());
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
    public String getStatus(Long id){
        Optional<Change> change = changeRepository.getChangeByPageId(id);
        if(!change.isPresent()){
            return "noting to check";
        }
        else{
            updateStatus(change.get());
            Change ch = changeRepository.getChangeByPageId(id).get();
            if(ch.getIs_confirmed() == null){
                return "still under review";
            }
            else{
                if(ch.getIs_confirmed()){
                    return "everything is fine";
                }
                else{
                    return "not accepted";
                }
            }
        }
    }

    private void updateStatus(Change change){
        List<Check> checks = checkRepository.getAllByChangeId(change.getId());
        int sum = 0;
        boolean flag = true;
        for (Check check : checks) {
            if (check.getIs_confirmed() == null) {
                sum++;
            } else {
                if (!check.getIs_confirmed()) {
                    flag = false;
                }
            }
        }
        if(sum == 0){
            changeRepository.setUserInfoById(flag, change.getId());
        }
    }

}
