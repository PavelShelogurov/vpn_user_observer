package home.project.healthchecker.exceptions;

public class FindUserInfoException extends CustomApplicationException {
    public FindUserInfoException(String message) {
        super(message);
    }

    public FindUserInfoException(String message, Throwable e) {
        super(message, e);
    }
}
