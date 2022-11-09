package home.project.healthchecker.service.users.storage;

import home.project.healthchecker.exceptions.RefreshStorageException;
import home.project.healthchecker.models.UserDescription;

import java.util.List;
import java.util.Optional;

/**
 * Interface for user storage
 */
public interface UserStorage {

    /**
     * @return info about all users in config
     */
    List<UserDescription> getAllUsers();

    /**
     * Find userDescription by ip
     * @param ip - ip address
     * @return Optional<UserDescription>
     */
    Optional<UserDescription> findByIp(String ip);

    /**
     * Find userDescription by name
     * @param name - username
     * @return Optional<UserDescription>
     */
    Optional<UserDescription> findByName(String name);

    /**
     * Save user description of new user
     * @param userDescription - description
     * @return - saved copy of userDescription
     */
    UserDescription save(UserDescription userDescription);
    /**
     * Hard refresh user storage info
     */
    List<UserDescription> refresh() throws RefreshStorageException;
}
