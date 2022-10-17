package home.project.healthchecker.models;

public class UserInfo {
    public String name;
    public String ip;
    public boolean isActive;
    public String description;

    public UserInfo(String name, String ip, boolean isActive) {
        this(name, ip, isActive, "no description");
    }

    public UserInfo(String name, String ip, boolean isActive, String description) {
        this.name = name;
        this.ip = ip;
        this.isActive = isActive;
        this.description = description;
    }
}
