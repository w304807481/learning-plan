package com.github.opensharing.javabase.stream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomUtils;

import com.github.opensharing.javabase.stream.model.User;

/**
 * StreamApiTester2
 *
 * @author jwen
 * Date 2025/8/7
 */
public class StreamApiTester2 {

    public static void main(String[] args) throws FileNotFoundException {
        //1、创建流
        List<User> users = UserFactory.buildUserList();
        //stream()
        System.out.println("\n顺序执行:");
        users.stream().forEach(System.out::println);
        //parallelStream()
        System.out.println("\n乱序执行:");
        users.parallelStream().forEach(System.out::println);
        //Stream.of
        System.out.println("\nStream.of()");
        Stream.of(1,2,3,4,5,6).forEach(System.out::println);
        //Stream.iterate
        System.out.println("\nStream.iterate()");
        Stream.iterate(1, x -> 2*x).limit(3).forEach(System.out::println);
        //Stream.generate
        System.out.println("\nStream.generate()");
        Stream.generate(RandomUtils::nextInt).limit(3).forEach(System.out::println);
        //Arrays.stream
        System.out.println("\nArrays.stream()");
        Arrays.stream(new int[]{1,2,3}).forEach(System.out::println);
        //BufferedReader.lines
        System.out.println("\nBufferedReader.lines()");
        new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("application.properties"))).lines().forEach(System.out::println);
        //Pattern.splitAsStream
        System.out.println("\nPattern.splitAsStream()");
        Pattern.compile(",").splitAsStream("a,b,c,d").forEach(System.out::println);
        //IntStream.range
        System.out.println("\nIntStream.range()");
        IntStream.range(1,10).forEach(System.out::println);
        //IntStream.rangeClosed
        System.out.println("\nIntStream.rangeClosed()");
        IntStream.rangeClosed(1,10).forEach(System.out::println);
        //Random().ints
        System.out.println("\nRandom().ints()");
        new Random().ints().limit(2).forEach(System.out::println);

        //2、中间操作
        System.out.println("\n中间操作: filter, skip, limit");
        users.stream()
                .filter(user -> user.getAge() > 20)
                .skip(1)
                .limit(1)
                .forEach(System.out::println);

        System.out.println("\n中间操作: distinct, sorted, mapToObj");
        Arrays.stream(new int[]{8,1,2,3,2,5,6,7,4,5})
                .distinct()
                .sorted()
                .mapToObj(x -> "str-"+x)
                .forEach(System.out::println);

        System.out.println("\n中间操作: peek");
        users.stream()
                .filter(user -> user.getSex().equals("男"))
                .peek(user -> user.setAge(80))
                .forEach(System.out::println);


        //3、终止操作
        System.out.println("\n终止操作: allMatch");
        boolean hasFemale = users.stream().anyMatch(user -> user.isFemale());
        System.out.println("有美女：" + hasFemale);

        System.out.println("\n终止操作: findFirst");
        Optional<User> user = users.stream().findFirst();
        System.out.println(user.get());

        System.out.println("\n终止操作: max");
        System.out.println(Stream.of(8,1,2,3,2,5,6,7,4,5).max(Integer::compareTo).get());

        System.out.println("\n终止操作: min");
        System.out.println(Stream.of(8,1,2,3,2,5,6,7,4,5).min(Integer::compareTo).get());
        System.out.println(Stream.of(8,1,2,3,2,5,6,7,4,5).min((x, y) -> {
            int min = Integer.compare(x, y);
            System.out.println("x=" + x + " y=" + y + " min: " + min);
            return min;
        }).get());//巨坑
        System.out.println("巨坑: " + Stream.of(8,1,2,3,2,5,6,7,4,5).min(Math::min).get());
        System.out.println("巨坑: " + Stream.of(8,1,2,3,2,5,6,7,4,5).min((x, y) -> {
            int min = Math.min(x, y);
            System.out.println("x=" + x + " y=" + y + " min: " + min);
            return min;
        }).get());//巨坑

        System.out.println("\n终止操作: reduce");
        Optional<Integer> total1 = Stream.of(1,2,3).reduce((x, y) -> {
            System.out.println("stream accumulator: x:" + x + "  y:" + y);
            return x + y;
        });
        System.out.println("total1: " + total1.get());
        int total2 = Stream.of(1,2,3).reduce(1, (x, y) -> {
            System.out.println("stream accumulator: x:" + x + "  y:" + y);
            return x + y;
        });
        System.out.println("total2: " + total2);
        int total3 = Stream.of(1, 2, 3, 4, 5, 6).parallel().reduce(1, (x, y) -> {
            System.out.println("stream accumulator: x:" + x + "  y:" + y);
            return x + y;
        }, (x, y) -> {//[2,3,4,5,6,7]
            System.out.println("stream combiner: x:" + x + "  y:" + y);
            return x + y;
        });
        System.out.println("total3: " + total3);

        System.out.println("\n终止操作: collect");
        List<Integer> list = Stream.of(8,1,2,3,2,5,6,7,4,5).sorted().collect(Collectors.toList());
        System.out.println(list);

        System.out.println("\n终止操作: forEachOrdered");
        Stream.of(8,1,2,3,2,5,6,7,4,5).unordered().parallel().distinct().forEachOrdered(System.out::println);

        //4、其他特殊操作
        System.out.println("\n其他特殊操作: sequential, parallel");
        Stream.of(8,1,2,3,2,5,6,7,4,5)
                .sorted()
                .sequential()
                .parallel()
                .sequential()
                .forEach(System.out::println);

        System.out.println("\n其他特殊操作: unordered");
        Stream.of(8,1,2,3,2,5,6,7,4,5).unordered().parallel().distinct().forEach(System.out::println);

        System.out.println("\n其他特殊操作: concat");
        Stream.concat(Stream.of(1,2,3), Stream.of(4,5,6)).forEach(System.out::println);

        System.out.println("\n其他特殊操作: toArray");
        User[] arr = Stream.of(8,1,2,3,2,5,6,7,4,5)
                .map(x -> new User(x, "xx", 0, "男"))
                .toArray(User[]::new);
        Arrays.stream(arr).forEach(System.out::println);

    }
}
