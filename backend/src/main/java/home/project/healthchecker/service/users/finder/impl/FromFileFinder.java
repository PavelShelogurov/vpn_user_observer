package home.project.healthchecker.service.users.finder.impl;

import home.project.healthchecker.exceptions.FindUserInfoException;
import home.project.healthchecker.models.UserDescription;
import home.project.healthchecker.service.users.finder.FindUserDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Find user info from file in system
 */
@Component
@ConditionalOnProperty(
        prefix = "users",
        name = "file.path"
)
public class FromFileFinder implements FindUserDescription {

    @Value("${description.separator:@@@}")
    private String descriptionSeparator;

    @Value("${users.file.path}")
    private String filePath;

    @Override
    public List<UserDescription> findUsersDescription() throws FindUserInfoException {

        List<UserDescription> users = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(new File(filePath).toPath());
            lines.stream().forEach(line -> users.add(createUserFromConfigLine(line)));
        } catch (IOException e) {
            throw new FindUserInfoException(e.getMessage());
        }
        return users;
    }


    private UserDescription createUserFromConfigLine(String line) {
        String[] description = line.trim().split(descriptionSeparator);
        return switch (description.length) {
            case 3 -> new UserDescription(description[0], description[1], description[2]);
            case 2 -> new UserDescription(description[0], description[1], null);
            case 1 -> new UserDescription(description[0], null, null);
            default -> new UserDescription("null", "error", "oops, empty line");
        };
    }
}
