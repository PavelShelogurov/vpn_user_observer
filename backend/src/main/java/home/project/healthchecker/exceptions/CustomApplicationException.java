package home.project.healthchecker.exceptions;

public class CustomApplicationException extends Exception{

    public CustomApplicationException(String message){
        super(message);
    }

    public CustomApplicationException(String message, Throwable e){
        super(message, e);
    }
}
