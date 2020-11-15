package com.github.opensharing.javabase.stream;

import java.util.List;

import com.github.opensharing.javabase.stream.model.User;
import com.google.common.collect.Lists;

/**
 * UserFactory
 *
 * @author jwen
 * Date 2020-09-20
 */
public class UserFactory {

    public static List<User> buildUserList() {
        return Lists.newArrayList(
                new User(1, "Tom", 23,  "男"),
                new User(2, "Jack", 20, "男"),
                new User(3, "Lily", 23, "女"),
                new User(4, "Bell", 26, "女"),
                new User(5, "Peter", 21, "男"));
    }
}
