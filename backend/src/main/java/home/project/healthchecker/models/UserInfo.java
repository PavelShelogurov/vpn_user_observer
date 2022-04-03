package home.project.healthchecker.models;

public class UserInfo {
    public String name;
    public String ip;
    public boolean isActive;

    public UserInfo(String name, String ip, boolean isActive) {
        this.name = name;
        this.ip = ip;
        this.isActive = isActive;
    }
}
