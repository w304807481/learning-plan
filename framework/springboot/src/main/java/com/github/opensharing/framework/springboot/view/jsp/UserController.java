package com.github.opensharing.framework.springboot.view.jsp;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.opensharing.framework.springboot.view.jsp.model.User;
import com.google.common.collect.Lists;

/**
 * UserController
 * <p>
 * 注意必须是@Controller， 不能是@RestController
 *
 * @author jwen
 * Date 2020-09-17
 */
@RequestMapping
@Controller
public class UserController {

    @RequestMapping("/users/list")
    public String showUsers(Model model) {
        List<User> users = Lists.newArrayList();
        users.add(new User(1, "Jack", 26));
        users.add(new User(2, "Roles", 26));
        users.add(new User(3, "Tom", 26));
        model.addAttribute("users", users);
        return "userlist";
    }
}
