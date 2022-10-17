package home.project.healthchecker.service.users.storage.impl;

import home.project.healthchecker.models.UserDescription;

import java.util.Date;
import java.util.List;

public class UserSimpleStorage {

    private Date updateTime;
    private List<UserDescription> cacheData;

    public UserSimpleStorage(Date updateTime, List<UserDescription> data) {
        this.updateTime = updateTime;
        this.cacheData = data;
    }

    public void updateCache(Date currentTime, List<UserDescription> data){
        this.updateTime = currentTime;
        this.cacheData = data;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public List<UserDescription> getCacheData() {
        return cacheData;
    }
}
