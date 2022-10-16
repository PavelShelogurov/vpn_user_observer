package home.project.healthchecker.service;

import home.project.healthchecker.config.UsersInfoConfig;
import home.project.healthchecker.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class HealthCheckService {

    @Autowired
    private UsersInfoConfig usersInfoConfig;
    @Autowired
    private UsersPinger usersPinger;

    public Set<String> getAllUsers() {
        return usersInfoConfig.getUser().keySet();
    }

    public UserInfo getUserInfo(String userName) throws UserNotFoundException, IOException {
        if (!usersInfoConfig.getUser().containsKey(userName)) {
            throw new UserNotFoundException(String.format("User '%s' not found", userName));
        }

        UserInfo userInfo = usersPinger.pingUser(userName, usersInfoConfig.getUser().get(userName));
        return userInfo;
    }

    public List<UserInfo> getInfoAboutAllUsers() throws IOException {
        return usersPinger.pingUsers(usersInfoConfig.getUser());
    }
}
