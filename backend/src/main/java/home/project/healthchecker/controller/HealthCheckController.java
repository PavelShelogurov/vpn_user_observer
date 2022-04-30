package home.project.healthchecker.controller;

import home.project.healthchecker.config.UsersInfoConfig;
import home.project.healthchecker.models.UserInfo;
import home.project.healthchecker.service.UsersPinger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class HealthCheckController {

    @Autowired
    private UsersInfoConfig usersInfoConfig;
    @Autowired
    private UsersPinger usersPinger;

    @GetMapping("/users")
    @CrossOrigin()
    public Set<String> getAllUsers() {
        return usersInfoConfig.getUser().keySet();
    }

    @GetMapping("/users/{name}")
    @CrossOrigin()
    public ResponseEntity<UserInfo> checkActiveUsers(@PathVariable String name) {
        if (!usersInfoConfig.getUser().containsKey(name)) {
            return new ResponseEntity<>(new UserInfo("Unknown user", "Unknown user", false),
                    HttpStatus.NOT_FOUND);
        }

        try {

            UserInfo userInfo = usersPinger.pingUser(name, usersInfoConfig.getUser().get(name));
            return new ResponseEntity<>(userInfo, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(new UserInfo("Error", e.getMessage(), false), HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/users-health")
    @CrossOrigin()
    public ResponseEntity<List<UserInfo>> checkActiveUsers() {
        try {
            List<UserInfo> userInfo = usersPinger.pingUsers(usersInfoConfig.getUser());
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (IOException e) {
            List<UserInfo> errorInfo = Collections.singletonList(new UserInfo("Error", e.getMessage(), false));
            return new ResponseEntity<> (errorInfo, HttpStatus.NOT_FOUND);
        }
    }
}
