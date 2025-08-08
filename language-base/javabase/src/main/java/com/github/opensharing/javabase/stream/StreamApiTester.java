package com.github.opensharing.javabase.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        testFilterOfStreamApi();

        //2.limit 限制结果集的数据量
        testLimitOfStreamApi();

        //3.sorted 排序
        testSortedOfStreamApi();

        //4.max min 获取结果中 某个值最大最小的的对象
        testMaxMinOfStreamApi();

        //5.map：对集合中的每个元素进行遍历，并且可以对其进行操作，转化为其他对象
        testMapOfStreamApi();

        //6.reduce: 也是对所有值进行操作，但它是将所有值，按照传入的处理逻辑，将结果处理合并为一个
        testReduceOfStreamApi();

        //7.collect方法以集合中的元素为基础，生成新的对象
        testCollectOfStreamApi();

    }

    /**
     * filter 对集合进行过滤
     */
    private static void testFilterOfStreamApi() {
        System.out.println("\n-------------------filter 对集合进行过滤-----------------------\n");
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

    /**
     * limit 限制结果集的数据量
     */
    private static void testLimitOfStreamApi() {
        System.out.println("\n-------------------limit 限制结果集的数据量-----------------------\n");
        List<User> list = UserFactory.buildUserList();
        System.out.println("集合初始状态：");
        list.stream().forEach(System.out::println);

        System.out.println("\n集合前3条 过滤后状态：");
        list.stream()
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n集合前3条， 性别是女性 过滤后状态：");
        list.stream()
                .limit(3)
                .filter(x -> x.isFemale())
                .forEach(System.out::println);
    }

    /**
     * sorted 排序
     */
    private static void testSortedOfStreamApi() {
        System.out.println("\n-------------------sorted 排序-----------------------\n");
        List<User> list = UserFactory.buildUserList();
        System.out.println("集合初始状态：");
        list.stream().forEach(System.out::println);

        System.out.println("\n集合按照年龄正序排列后状态：");
        list.stream()
                .sorted((x, y) -> (x.getAge() - y.getAge()))
                .forEach(System.out::println);
        //or
        System.out.println("\nor via Comparator.comparingInt：");
        list.stream()
                .sorted(Comparator.comparingInt(User::getAge))
                .forEach(System.out::println);
    }

    /**
     * max min 获取结果中 某个值最大最小的的对象
     * <p>
     * 注意：当集合里有多个满足条件的最大最小值时，只会返回一个对象
     */
    private static void testMaxMinOfStreamApi() {
        System.out.println("\n-------------------max min 获取结果中 某个值最大最小的的对象-----------------------\n");
        List<User> list = UserFactory.buildUserList();
        System.out.println("集合初始状态：");
        list.stream().forEach(System.out::println);

        User maxAgeUser = list.stream().max(Comparator.comparingInt(User::getAge)).get();
        System.out.println("\n集合取年龄最大的人：\n" + maxAgeUser);

        User minAgeUser = list.stream().min(Comparator.comparingInt(User::getAge)).get();
        System.out.println("\n集合取年龄最小的人：\n" + minAgeUser);
    }

    /**
     * map：对集合中的每个元素进行遍历，并且可以对其进行操作，转化为其他对象
     * <p>
     * map跟foreach的区别：
     * map对原来的集合A操作后返回一个新的对象， 并构成集合B。此集合B和原集合A无任何关系，且不会改变原集合A的任何东西
     * foreach操作的是原集合A，因为是结束型API，相当于返回的还是原集合A
     */
    private static void testMapOfStreamApi() {
        System.out.println("\n-------------------map：对集合中的每个元素进行遍历，并且可以对其进行操作，转化为其他对象-----------------------\n");
        List<User> list = UserFactory.buildUserList();
        System.out.println("集合初始状态：");
        list.stream().forEach(System.out::println);

        System.out.println("\n集合中每个人年龄+10岁后的状态：");
        list.stream()
                .map(x -> new User(x.getId(), x.getName(), (x.getAge() + 10), x.getSex()))
                .forEach(System.out::println);

        System.out.println("\n原集合当前状态：");
        list.stream().forEach(System.out::println);
    }

    /**
     * reduce: 也是对所有值进行操作，但它是将所有值，按照传入的处理逻辑，将结果处理合并为一个
     */
    private static void testReduceOfStreamApi() {
        System.out.println("\n-------------------reduce: 也是对所有值进行操作，但它是将所有值，按照传入的处理逻辑，将结果处理合并为一个-----------------------\n");
        //相当于foreach遍历操作结果值
        Integer out = Stream.of(1,2,3).reduce((result, item) -> {
            if (item >= 3) {
                result = result + item;
            }
            return result;
        }).get();
        System.out.println("串行计算:" + out);

        //相当于给定初始结果值，foreach遍历操作结果值
        Integer sum = Stream.of(1,2,3).reduce(2, (result, item) -> result + item);
        System.out.println("带起始值串行计算:" + sum);

        //相当于给定初始结果值，两个foreach遍历操作结果值
        int str = Stream.of(1,2,3).parallel().reduce(2, (result, item) -> {
            System.out.println(result + "_" + item);
            return result + item;
        } , (result, item) -> {
            //注：只有并行parallel下才会进入此方法
            System.out.println(result + "&" + item);
            return result + item ;
        });
        System.out.println("带起始值并行计算:" + str);
    }

    /**
     * collect方法以集合中的元素为基础，生成新的对象
     */
    private static void testCollectOfStreamApi() {
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);
    }

    /**
     * skip跳过一些记录
     */
    private static void testSkipOfStreamApi() {
        String[] array = {"abc", "def", "ghi", "jkl"};
        Stream.of(array).skip(2).forEach(System.out::println);
    }
}
