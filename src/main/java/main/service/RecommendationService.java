package main.service;

import main.entity.Page;
import main.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    public RecommendationService(){};

    public String getAnswer(Page page, User user){
        String role = page.getRole();
        String uRole = user.getRole();
        if(chechAccess(role, uRole)){
            return page.getText();
        }
        else{
            return "no access";
        }
    }

    private boolean chechAccess(String role, String uRole){
        return role.equals(uRole) || uRole.equals("admin");
    }

    public String getRecommendations(List<Page> pages, User user, String name){
        StringBuilder answer = new StringBuilder();
        for (Page page : pages) {
            if (chechAccess(page.getRole(), user.getRole()) && (page.getName().contains(name) || page.getText().contains(name))) {
                answer.append(page.getName()).append("\n");
            }
        }
        if(answer.toString().length() == 0){
            return "no matches";
        }
        else{
            return answer.toString();
        }

    }
}
