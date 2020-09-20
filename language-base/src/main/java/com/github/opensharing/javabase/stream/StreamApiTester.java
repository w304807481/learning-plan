package com.github.opensharing.javabase.stream;

import java.util.List;
import java.util.function.Predicate;

import com.github.opensharing.javabase.stream.model.User;

/**
 * StreamApiTester
 *
 * @author jwen
 * Date 2020-09-20
 */
public class StreamApiTester {

    public static void main(String[] args) {
        //1.filter 对集合进行过滤
        testStreamFilter();

    }

    /**
     * filter 对集合进行过滤
     */
    public static void testStreamFilter() {
        List<User> list = UserFactory.buildUserList();
        System.out.println("集合初始状态：");
        list.stream().forEach(System.out::println);

        System.out.println("\n集合年龄>22过滤后状态：");
        list.stream()
                .filter((user) -> user.getAge() > 22)
                .forEach((user) -> System.out.println(user));

        System.out.println("\n集合年龄>22 and 性别=女 过滤后状态：");
        System.out.println("简单写法：");
        list.stream()
                .filter((user) -> user.getAge() > 22)
                .filter((user) -> user.isFemale())
                .forEach((user) -> System.out.println(user));
        //or
        System.out.println("\nor via Predicate：");
        Predicate<User> agePredicate = (user) -> user.getAge() > 22;
        Predicate<User> sexPredicate = (user) -> user.isFemale();
        list.stream()
                .filter(agePredicate)
                .filter(sexPredicate)
                .forEach((user) -> System.out.println(user));
        //or
        System.out.println("\nor agePredicate.and(sexPredicate)：");
        list.stream()
                .filter(agePredicate.and(sexPredicate))
                .forEach((user) -> System.out.println(user));

    }
}
