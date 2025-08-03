package com.github.opensharing.javabase.lambda;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 常见的函数式接口及用法
 */
public class FunctionalInterfaceLambdaTester {

    public static void main(String[] args) {
        //1 Runnable/Callable
        testRunnable();
        testCallable();

        //2 Supplier/Consumer
        testSupplier();
        testConsumer();

        //3 Comparator
        testComparator();

        //4 Predicate
        testPredicate();

        //4 Function
        testFunction();
    }

    /**
     * Callable
     */
    private static void testRunnable() {
        new Thread(() -> System.out.println(Thread.currentThread().getName() + ": hello")).start();
    }

    /**
     * Runnable
     */
    private static void testCallable() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> System.out.println(Thread.currentThread().getName() + ": hello"));
    }

    /**
     * Supplier
     */
    private static void testSupplier() {
        int[] array = new int[]{5, 2, 1, 6, 8};
        int max = getMax(() -> {
            int temp = array[0];

            for (int i = 0; i < array.length; i++) {
                temp = Math.max(temp, array[i]);
            }

            return temp;
        });
        System.out.println(max);
    }

    private static int getMax(Supplier<Integer> supplier) {
        return supplier.get();
    }

    /**
     * Consumer
     */
    private static void testConsumer() {
        consumerString("hello", x -> {
            System.out.println(x);
        });

        consumerString2("hello",
                x -> System.out.println(x.toLowerCase()),
                x -> System.out.println(x.toUpperCase()));
    }

    private static void consumerString(String str, Consumer<String> consumer) {
        consumer.accept(str);
    }

    private static void consumerString2(String str, Consumer<String> consumer1, Consumer<String> consumer2) {
        consumer1.andThen(consumer2).accept(str);
    }

    /**
     * Comparator
     */
    private static void testComparator() {
        String[] array = new String[]{"aaa", "bbbbbb", "cc", "d", "eeeeeeee", "ffff"};
        Arrays.sort(array, (x, y) -> x.length() - y.length());
        System.out.println(Arrays.toString(array));
    }

    /**
     * Predicate
     */
    private static void testPredicate() {
        System.out.println(andPredicate(x -> x.contains("H"), x -> x.contains("w")));
        System.out.println(orPredicate(x -> x.contains("H"), x -> x.contains("w")));
        System.out.println(negatePredicate(x -> x.contains("w")));
    }

    private static boolean andPredicate(Predicate<String> predicate1, Predicate<String> predicate2) {
        return predicate1.and(predicate2).test("HelloWorld");
    }

    private static boolean orPredicate(Predicate<String> predicate1, Predicate<String> predicate2) {
        return predicate1.or(predicate2).test("HelloWorld");
    }

    private static boolean negatePredicate(Predicate<String> predicate) {
        return predicate.negate().test("HelloWorld");
    }

    /**
     * Function
     */
    private static void testFunction() {
        System.out.println(applyFunctions(
                x -> Math.abs(x),
                x -> 2*x)
        );
    }

    private static Integer applyFunctions(Function<Integer, Integer> function1, Function<Integer, Integer> function2) {
        return function1.andThen(function2).apply(-5);
    }
}
