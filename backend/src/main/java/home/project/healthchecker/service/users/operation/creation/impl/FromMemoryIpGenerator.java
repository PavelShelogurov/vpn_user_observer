package home.project.healthchecker.service.users.operation.creation.impl;

import home.project.healthchecker.exceptions.ResourceNotFoundException;
import home.project.healthchecker.service.users.operation.creation.IpGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class FromMemoryIpGenerator implements IpGenerator {

    private List<String> availableIps;

    public FromMemoryIpGenerator() {
        availableIps = new ArrayList<>();
        for (int i = 2; i < 256; i++) {
            availableIps.add(String.format("10.0.0.%d/32", i));
        }
    }

    @Override
    public String getFreeIp(Set<String> usedIpAddresses) throws ResourceNotFoundException {
        availableIps.removeAll(usedIpAddresses);
        if (availableIps.size() != 0) {
            return availableIps.get(0);
        } else {
            throw new ResourceNotFoundException("Not found available ips");
        }
    }
}
