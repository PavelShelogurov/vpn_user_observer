package home.project.healthchecker.service.users.operation.creation.impl;

import home.project.healthchecker.HealthCheckerApplication;
import home.project.healthchecker.exceptions.CreateUserException;
import home.project.healthchecker.models.CreateUserRequest;
import home.project.healthchecker.models.CreateUserResult;
import home.project.healthchecker.service.users.operation.creation.CreateUser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HealthCheckerApplication.class,
        properties = {
        "health.check.timeout=2000",
//        "users.file.path=D:\\JAVA\\Java_projects\\VPN_health_checker\\backend\\src\\main\\resources\\users.config",
        "cache.update.period=10",
        "description.separator=@@@",
        "wg.config.path=D:\\\\JAVA\\\\Java_projects\\\\VPN_health_checker\\\\backend\\\\src\\\\test\\\\resources\\\\wg0.conf",
        "wg.server.public.key=serverPUBkey",
        "wg.server.ip=77.77.77.77",
        "wg.server.port=9999",
        "spring.profiles.default=prod",
        "spring.profiles.active=dev"
})
public class WgUserCreatorTest {

    @Autowired
    private CreateUser createUser;

    @Test
    public void createUserTest(){

        CreateUserRequest createRequest = new CreateUserRequest("agregator", "super-puser zver");
        try {
            CreateUserResult createResult = createUser.createUser(createRequest);
            System.out.println(createResult.userDescription().ip());
            System.out.println(createResult.config());
        } catch (CreateUserException e) {
            System.out.println(e.getMessage());
        }
    }

}