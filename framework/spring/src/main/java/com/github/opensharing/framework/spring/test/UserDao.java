package com.github.opensharing.framework.spring.test;

/**
 * UserDao
 *
 * @author jwen
 * Date 2023/11/10
 */
public class UserDao {

    private String limit;

    public int save() {
        System.out.println("UserDao save 成功了。。。");
        return 1;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
