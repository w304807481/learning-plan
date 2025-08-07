package com.github.opensharing.framework.spring.test;

/**
 * UserService
 *
 * @author jwen
 * Date 2023/11/10
 */
public class UserService {

    private String config;

    private UserDao UserDao;

    public boolean save() {
        System.out.println("config: " + config);
        return UserDao.save() > 0;
    }

    public UserDao getUserDao() {
        return UserDao;
    }

    public void setUserDao(UserDao userDao) {
        UserDao = userDao;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
