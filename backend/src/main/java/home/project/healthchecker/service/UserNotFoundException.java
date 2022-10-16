package home.project.healthchecker.service;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String msg){
        super(msg);
    }
}
