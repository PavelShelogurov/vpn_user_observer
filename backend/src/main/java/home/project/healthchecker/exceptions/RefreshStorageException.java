package home.project.healthchecker.exceptions;

public class RefreshStorageException extends CustomApplicationException{
    public RefreshStorageException(String message) {
        super(message);
    }

    public RefreshStorageException(String message, Throwable e) {
        super(message, e);
    }
}
