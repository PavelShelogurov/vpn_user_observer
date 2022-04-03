package home.project.healthchecker.service;

import home.project.healthchecker.models.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UsersPinger {
    @Value("${health.check.timeout}")
    private int timeout;

    public List<UserInfo> pingUsers(Map<String, String> usersConfigMapNameHost) throws IOException {

        List<UserInfo> result = new ArrayList<>();

        for (Map.Entry<String, String> user : usersConfigMapNameHost.entrySet()) {
            InetAddress[] address = InetAddress.getAllByName(user.getValue());
            boolean isReachable = address[0].isReachable(timeout);
            result.add(new UserInfo(user.getKey(), user.getValue(), isReachable));
        }
        return result;
    }
}
