package home.project.healthchecker.service.users.storage.impl;

import home.project.healthchecker.exceptions.FindUserInfoException;
import home.project.healthchecker.exceptions.RefreshStorageException;
import home.project.healthchecker.models.UserDescription;
import home.project.healthchecker.service.users.finder.FindUserDescription;
import home.project.healthchecker.service.users.storage.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * In memory cache for store user description
 */
@Component
public class UserSimpleInMemoryCache implements UserStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSimpleInMemoryCache.class);
    @Value("${cache.update.period}")
    private Long updateCachePeriod;
    private FindUserDescription findUserDescription;

    private UserSimpleStorage userSimpleStorage;

    public UserSimpleInMemoryCache(FindUserDescription findUserDescription) throws RefreshStorageException {
        this.findUserDescription = findUserDescription;
        try {
            updateUserCache();
        } catch (FindUserInfoException e) {
            LOGGER.error("Error while refresh in memory user cache", e);
            throw new RefreshStorageException("Error while refresh in memory user cache", e);
        }
    }


    @Override
    public List<UserDescription> getAllUsers() {
        checkCacheAndUpdate();
        return userSimpleStorage.getCacheData();
    }

    @Override
    public Optional<UserDescription> findByIp(String ip) {
        checkCacheAndUpdate();
        return userSimpleStorage.getCacheData().stream().filter(user -> user.ip().equals(ip)).findFirst();
    }

    @Override
    public Optional<UserDescription> findByName(String name) {
        checkCacheAndUpdate();
        return userSimpleStorage.getCacheData().stream().filter(user -> user.name().equals(name)).findFirst();
    }


    @Override
    public List<UserDescription> refresh() throws RefreshStorageException {
        try {
            updateUserCache();
            return userSimpleStorage.getCacheData();
        } catch (FindUserInfoException e) {
            LOGGER.error("Error while refresh in memory user cache", e);
            throw new RefreshStorageException("Error while refresh in memory user cache", e);
        }
    }

    private void updateUserCache() throws FindUserInfoException {
        List<UserDescription> users = findUserDescription.findUsersDescription();
        if(this.userSimpleStorage == null){
            this.userSimpleStorage = new UserSimpleStorage(new Date(), users);
        } else {
            this.userSimpleStorage.updateCache(new Date(), users);
        }
    }

    public void checkCacheAndUpdate(){
        if(!isCacheDataFresh()){
            try {
                updateUserCache();
            } catch (FindUserInfoException e) {
                LOGGER.error("Error while refresh in memory user cache", e);
            }
        }
    }

    /**
     * If diff between current time and cache update time more then updateCachePeriod return false
     * @return if false - update cache, if true don't update
     */
    private boolean isCacheDataFresh(){
        return new Date().getTime() - this.userSimpleStorage.getUpdateTime().getTime() <= (updateCachePeriod * 60 * 1000);
    }
}
