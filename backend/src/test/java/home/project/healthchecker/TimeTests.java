package home.project.healthchecker;

import home.project.healthchecker.models.UserInfo;
import home.project.healthchecker.service.UsersPinger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class TimeTests {

    @Autowired
    UsersPinger usersPinger;
    @Autowired
    UsersInfoConfig usersInfoConfig;

    @Test
    public void pingSpeed(){
        StopWatch timer = new StopWatch();
        List<UserInfo> userInfos = null;
        timer.start("Ping users");
        try {
            userInfos = usersPinger.pingUsers(usersInfoConfig.getUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer.stop();

        System.out.println(timer.prettyPrint());

        System.out.println("Количество пропингованных пользователей:" + userInfos.size());
        userInfos.forEach(u -> System.out.println(u.name + " : " + u.ip + " : " + u.isActive));
    }

}
