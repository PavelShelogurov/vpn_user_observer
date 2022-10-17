package home.project.healthchecker.service.users.finder;

import home.project.healthchecker.exceptions.FindUserInfoException;
import home.project.healthchecker.models.UserDescription;

import java.util.List;

/**
 * Interface for search user info in different resources (file, db or etc.)
 */
public interface FindUserDescription {
    List<UserDescription> findUsersDescription() throws FindUserInfoException;
}
