package com.github.opensharing.javabase.lambda;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.opensharing.javabase.lambda.model.User;

/**
 * LambdaTester
 *
 * @author jwen
 * Date 2020-09-20
 */
public class LambdaTester {


    public static void main(String[] args) {

        //1.无参表达式
        testNoArgsLambda();

        //2.对象::实例方法
        testMethodRefLambda();

        //3.类::静态方法
        testClazzStaticMethodRefLambda();

        //4.类::实例方法
        testClazzInstanceMethodRefLambda();

        //5.无参的构造方法就是类::实例方法模型
        testClazzNoArgsConstructorRefLambda();
    }

    /**
     * 无参表达式
     */
    private static void testNoArgsLambda() {
        System.out.println("\n-------------------无参表达式-----------------------\n");
        //old
        new Thread((new Runnable() {
            @Override
            public void run() {
                System.out.println("一般写法结果：匿名内部类 实现线程");
            }
        })).start();

        //lambda
        new Thread(() -> System.out.println("简化写法结果：java8 lambda实现线程")).start();
    }

    /**
     * 对象::实例方法
     */
    public static void testMethodRefLambda() {
        System.out.println("\n-------------------对象::实例方法-----------------------\n");
        List<User> list = UserFactory.buildUserList();
        Consumer<User> changeAge = e -> e.setAge(e.getAge() + 3);
        list.forEach(changeAge);

        //一般写法
        System.out.println("对象调用实例方法一般写法结果:");
        list.forEach((x) -> System.out.println(x));

        //简化写法
        System.out.println("对象调用实例方法简化写法结果:");
        list.forEach(System.out::println);
    }

    /**
     * 类::静态方法
     */
    public static void testClazzStaticMethodRefLambda() {
        System.out.println("\n-------------------类::静态方法----------------------\n");
        //一般写法
        Function<Integer, String> sf =  (x) -> String.valueOf(x);
        System.out.println("类调用静态方法一般写法结果: " + sf.apply(1000));

        //简化写法
        Function<Integer, String> sf2 = String::valueOf;
        System.out.println("类调用静态方法简化写法结果: " + sf2.apply(1000));
    }

    /**
     * 类::实例方法
     */
    public static void testClazzInstanceMethodRefLambda() {
        System.out.println("\n-------------------类::实例方法----------------------\n");
        //一般写法
        BiPredicate<String, String> bp = (x, y) -> x.equals(y);
        System.out.println("类调用实例方法一般写法结果: " + bp.test("abc", "ABC"));

        //简化写法
        BiPredicate<String, String> bp2 = String::equals;
        System.out.println("类调用实例方法简化写法结果: " + bp2.test("abc", "ABC"));
    }

    /**
     * 无参的构造方法就是类::实例方法模型
     */
    public static void testClazzNoArgsConstructorRefLambda() {
        System.out.println("\n-------------------无参的构造方法就是类::实例方法模型----------------------\n");
        //一般写法
        Supplier<User> us = () -> new User();
        User user1 = us.get();
        System.out.println("无参的构造方法一般写法结果:" + user1);

        //简化写法
        Supplier<User> us2 = User::new;
        User user2 = us2.get();
        System.out.println("无参的构造方法简化写法结果:" + user2);
    }
}
