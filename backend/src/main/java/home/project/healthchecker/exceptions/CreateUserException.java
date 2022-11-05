package home.project.healthchecker.exceptions;

public class CreateUserException extends CustomApplicationException {
    public CreateUserException(String message) {
        super(message);
    }

    public CreateUserException(String message, Throwable e) {
        super(message, e);
    }
}
