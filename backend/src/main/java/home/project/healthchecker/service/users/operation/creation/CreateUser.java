package home.project.healthchecker.service.users.operation.creation;

import home.project.healthchecker.exceptions.CreateUserException;
import home.project.healthchecker.models.CreateUserRequest;
import home.project.healthchecker.models.CreateUserResult;

/**
 * Creating user to network
 */
public interface CreateUser {

    CreateUserResult createUser(CreateUserRequest request) throws CreateUserException;

}