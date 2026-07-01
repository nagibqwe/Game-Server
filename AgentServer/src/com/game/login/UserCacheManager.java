package com.game.login;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author hewei@haowan123.com
 */
public class UserCacheManager {

    private static CacheManager cacheManager;
    private static Cache userCache;

    private static final Logger log = LogManager.getLogger(UserCacheManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        UserCacheManager manager;

        Singleton() {
            this.manager = new UserCacheManager();
        }

        UserCacheManager getProcessor() {
            return manager;
        }
    }

    public static UserCacheManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void initialize(String filePath) {
        cacheManager = new CacheManager(filePath);
        userCache = cacheManager.getCache("User");
    }

    public void put(String userName, LoginDataBean data) {
        Element element = new Element(userName, data);
        userCache.put(element);
    }

    public void remove(String userName) {
        userCache.remove(userName);
    }

    public void removeByUserId(long userId) {
        LoginDataBean bean = null;
        String deleteKey = null;
        for (Object key : userCache.getKeys()) {
            bean = get((String) key);
            if (bean == null) {
                continue;
            }
            if (bean.getUserId() != userId) {
                continue;
            }
            deleteKey = (String) key;
            break;
        }
        if (deleteKey == null) {
            return;
        }
        remove(deleteKey);
    }

    public LoginDataBean get(String userName) {
        Element element = userCache.get(userName);
        if (element == null) {
            return null;
        }
        return (LoginDataBean) element.getObjectValue();
    }

    public LoginDataBean getByUserId(long userId) {
        LoginDataBean bean = null;
        for (Object key : userCache.getKeys()) {
            bean = get((String) key);
            if (bean == null) {
                continue;
            }
            if (bean.getUserId() != userId) {
                continue;
            }
            return bean;
        }
        return null;
    }
}
