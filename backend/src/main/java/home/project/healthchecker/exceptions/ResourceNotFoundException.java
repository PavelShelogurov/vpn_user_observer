package home.project.healthchecker.exceptions;

public class ResourceNotFoundException extends CustomApplicationException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
