package home.project.healthchecker.service;

import home.project.healthchecker.exceptions.FindUserInfoException;
import home.project.healthchecker.exceptions.RefreshStorageException;
import home.project.healthchecker.models.UserDescription;
import home.project.healthchecker.models.UserInfo;
import home.project.healthchecker.service.users.storage.UserStorage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HealthCheckService {

    private UserStorage userStorage;
    private UsersPinger usersPinger;

    public HealthCheckService(UserStorage userStorage, UsersPinger usersPinger) {
        this.userStorage = userStorage;
        this.usersPinger = usersPinger;
    }

    public Set<String> getUsersName() throws FindUserInfoException {
        List<UserDescription> users = userStorage.getAllUsers();
        return users.stream().map(user -> user.name()).collect(Collectors.toSet());
    }

    public UserInfo getUserInfo(String userName) throws UserNotFoundException, IOException {
        Optional<UserDescription> userOptional = userStorage.findByName(userName);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException(String.format("User '%s' not found", userName));
        }
        UserDescription user = userOptional.get();
        UserInfo userInfo = usersPinger.pingUser(user);
        return userInfo;
    }

    public List<UserInfo> getInfoAboutAllUsers() throws IOException {
        return usersPinger.pingUsers(userStorage.getAllUsers());
    }

    public List<UserInfo> hardUpdateUsersInfo() throws RefreshStorageException, IOException {
        List<UserDescription> freshUsers = userStorage.refresh();
        return usersPinger.pingUsers(freshUsers);
    }
}
