package home.project.healthchecker.service;

import home.project.healthchecker.models.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class UsersPinger {
    @Value("${health.check.timeout}")
    private int timeout;

    public List<UserInfo> pingUsers(Map<String, String> usersConfigMapNameHost) throws IOException {

        List<UserInfo> result = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(usersConfigMapNameHost.size());

        usersConfigMapNameHost.forEach((hostName, ipAddress) -> {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress[] address = InetAddress.getAllByName(ipAddress);
                        boolean isReachable = address[0].isReachable(timeout);
                        result.add(new UserInfo(hostName, ipAddress, isReachable));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        });

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            //waiting stop executorService
        }

        return result;
    }
}
