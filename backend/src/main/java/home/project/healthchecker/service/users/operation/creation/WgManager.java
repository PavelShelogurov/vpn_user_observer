package home.project.healthchecker.service.users.operation.creation;

import home.project.healthchecker.exceptions.CreateUserException;
import home.project.healthchecker.models.WgKeys;

public interface WgManager {
    WgKeys generateKeys() throws CreateUserException;
    void restartWgDemon() ;
}
