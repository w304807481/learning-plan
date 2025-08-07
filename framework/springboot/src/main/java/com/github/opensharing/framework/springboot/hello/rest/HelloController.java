package com.github.opensharing.framework.springboot.hello.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.opensharing.framework.springboot.hello.dto.User;
import com.github.opensharing.framework.springboot.hello.service.UserService;

/**
 * HelloController
 *
 * @author jwen
 * Date 2020-09-09
 */
@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        User user = userService.getById(1);
        return (user == null) ? "world" : user.toString();
    }
}
