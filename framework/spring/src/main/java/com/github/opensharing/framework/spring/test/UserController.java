package com.github.opensharing.framework.spring.test;

import com.github.opensharing.framework.spring.context.support.ClassXmlApplicationContext;

/**
 * UserController
 *
 * @author jwen
 * Date 2023/11/10
 */
public class UserController {

    private UserService userService;

    public void save() {
        userService.save();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) throws Exception {
        ClassXmlApplicationContext context = new ClassXmlApplicationContext("beans.xml");
        UserController userController = context.getBean("userController", UserController.class);
        userController.save();
    }
}
