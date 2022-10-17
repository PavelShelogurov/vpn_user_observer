package home.project.healthchecker.controller;

import home.project.healthchecker.exceptions.FindUserInfoException;
import home.project.healthchecker.exceptions.RefreshStorageException;
import home.project.healthchecker.models.UserDescription;
import home.project.healthchecker.models.UserInfo;
import home.project.healthchecker.service.HealthCheckService;
import home.project.healthchecker.service.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class HealthCheckController {

    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/users")
    @CrossOrigin()
    public ResponseEntity<?> getAllUsers() {
        try {
            return new ResponseEntity<>(healthCheckService.getUsersName(), HttpStatus.OK);
        } catch (FindUserInfoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/users/{name}")
    @CrossOrigin()
    public ResponseEntity<UserInfo> checkUser(@PathVariable String name) {
        try {
            UserInfo userInfo = healthCheckService.getUserInfo(name);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            UserInfo errorUserInfo = new UserInfo(name, e.getMessage(), false);
            return new ResponseEntity<>(errorUserInfo, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/users-health")
    @CrossOrigin()
    public ResponseEntity<List<UserInfo>> checkActiveUsers() {
        try {
            List<UserInfo> usersInfo = healthCheckService.getInfoAboutAllUsers();
            return new ResponseEntity<>(usersInfo, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/refresh/users")
    @CrossOrigin
    public ResponseEntity<?> updateUsersInfo(){
        try {
            List<UserInfo> userInfos = healthCheckService.hardUpdateUsersInfo();
            return new ResponseEntity<>(userInfos, HttpStatus.OK);
        } catch (RefreshStorageException | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.I_AM_A_TEAPOT);
        }
    }
}
