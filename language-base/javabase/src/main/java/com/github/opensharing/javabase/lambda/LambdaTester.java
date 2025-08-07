package com.github.opensharing.javabase.lambda;

import java.io.PrintStream;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.opensharing.javabase.lambda.model.Tourist;
import com.github.opensharing.javabase.lambda.model.User;

/**
 * LambdaTester
 *
 * @author jwen
 * Date 2020-09-20
 */
public class LambdaTester {


    public static void main(String[] args) {

        //0.基本用法
        testBaseLambda();
        testNoArgsLambda();

        //1.类::静态方法
        testClazzStaticMethodRefLambda();

        //2.对象::实例方法
        testClazzInstanceMethodRefLambda();
        testClazzInstanceMethodRefLambda2();

        //3.构造方法 类::new
        testClazzNoArgsConstructorRefLambda();
        testClazzArgsConstructorRefLambda();

        //4.子类引用父类方法
        testSuperClazzMethodRefLambda();

        //5.子类类引用自己方法
        testThisClazzMethodRefLambda();

        //6.引用数组构造
        testArrayConstructorRefLambda();
    }

    /**
     * 基础用法
     */
    private static void testBaseLambda() {
        System.out.println("\n-------------------基本用法-----------------------\n");
        CalculateOperator operator = new CalculateOperator() {

            @Override
            public int calc(int a, int b) {
                return a + b;
            }
        };

        operator = (x, y) -> x + y;

        System.out.println(operator.calc(3, 5));
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
    public static void testClazzInstanceMethodRefLambda2() {
        System.out.println("\n-------------------对象::实例方法-----------------------\n");
        List<User> list = UserFactory.buildUserList();
        Consumer<User> changeAge = e -> e.setAge(e.getAge() + 3);
        list.forEach(changeAge);

        //一般写法
        PrintStream out = System.out;
        System.out.println("对象调用实例方法一般写法结果:");
        list.forEach((x) -> out.println(x));

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
     * 无参的构造方法就是类::new
     */
    public static void testClazzNoArgsConstructorRefLambda() {
        System.out.println("\n-------------------无参的构造方法就是类::new----------------------\n");
        //一般写法
        Supplier<User> us = () -> new User();
        User user1 = us.get();
        System.out.println("无参的构造方法一般写法结果:" + user1);

        //简化写法
        Supplier<User> us2 = User::new;
        User user2 = us2.get();
        System.out.println("无参的构造方法简化写法结果:" + user2);
    }

    /**
     * 有参的构造方法就是类::new
     */
    public static void testClazzArgsConstructorRefLambda() {
        System.out.println("\n-------------------有参的构造方法就是类::new----------------------\n");
        //一般写法
        printTourist("老张", name -> new Tourist(name));

        //简化写法
        printTourist("老张", Tourist::new);
    }

    private static  void printTourist(String name, TouristBuilder touristBuilder) {
        System.out.println(touristBuilder.build(name));
    }

    private interface TouristBuilder {
        Tourist build(String name);
    }

    public static void testSuperClazzMethodRefLambda() {
        System.out.println("\n-------------------super::method----------------------\n");
        Tourist tourist = new Tourist("老张");
        tourist.move();
    }

    public static void testThisClazzMethodRefLambda() {
        System.out.println("\n-------------------this::method----------------------\n");
        Tourist tourist = new Tourist("老张");
        tourist.stay();
    }

    public static void testArrayConstructorRefLambda() {
        System.out.println("\n--------------------数组构造 type[]::new---------------------\n");
        int[] array = createArray(10, len -> {
            return new int[len];
        });
        System.out.println(array.length);
        array = createArray(10, int[] :: new);
        System.out.println(array.length);
    }

    private static int[] createArray(int len, ArrayBuilder arrayBuilder) {
        return arrayBuilder.build(len);
    }

}
