package home.project.healthchecker.controller;

import home.project.healthchecker.config.UsersInfoConfig;
import home.project.healthchecker.models.UserInfo;
import home.project.healthchecker.service.UsersPinger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
public class HealthHeckController {

    @Autowired
    private UsersInfoConfig usersInfoConfig;
    @Autowired
    private UsersPinger usersPinger;

    @GetMapping("/health")
    @CrossOrigin()
    public List<UserInfo> checkActiveUsers() {
        try {
            return usersPinger.pingUsers(usersInfoConfig.getUser());
        } catch (IOException e) {
            return Collections.singletonList(new UserInfo("Error", e.getMessage(), false));
        }
    }
}
