package home.project.healthchecker.exceptions;

public class ConsoleCommandException extends CustomApplicationException {
    public ConsoleCommandException(String message) {
        super(message);
    }

    public ConsoleCommandException(String message, Throwable e) {
        super(message, e);
    }
}
