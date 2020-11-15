package com.github.opensharing.javabase.lambda;

import java.util.List;

import com.github.opensharing.javabase.lambda.model.User;
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
                new User(1, "张三", 20),
                new User(2, "李四", 22),
                new User(3, "王五", 24),
                new User(4, "赵六", 26));
    }
}
