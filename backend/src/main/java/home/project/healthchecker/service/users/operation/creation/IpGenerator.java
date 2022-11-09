package home.project.healthchecker.service.users.operation.creation;

import home.project.healthchecker.exceptions.ResourceNotFoundException;

import java.util.Set;

public interface IpGenerator {
    String getFreeIp(Set<String> usedIpAddresses) throws ResourceNotFoundException;
}
