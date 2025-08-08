package com.github.opensharing;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ReactorTester
 *
 * @author jwen
 * Date 2025/8/8
 */
public class ReactorTester {

    public static void main(String[] args) throws InterruptedException {
        //testFlux();
        //testMono();
        //testFluxFrom();
        //testMonoDefer();
        //testFluxSubscribe();
        //testFluxMap();
        //testFluxFilter();
        //testFluxCollect();
        //testFluxOthers();
        //testFluxStreamMerge();
        //testFluxStreamBatch();
        //testFluxStreamFlatMap();
        //testFluxStreamSample();
        //testFluxStreamBlock();
        testFluxStreamMaterialize();
    }

    /**
     * 测试Flux创建流的一些接口
     */
    public static void testFlux() {
        System.out.println("\nFlux.just");
        Flux<String> flux1 = Flux.just("hello", "wolrd");
        flux1.subscribe(System.out::println);

        System.out.println("\nFlux.fromArray");
        Flux<String> flux2 = Flux.fromArray(new String[]{"Flux", "fromArray"});
        flux2.subscribe(System.out::println);

        System.out.println("\nFlux.fromIterable");
        Flux<String> flux3 = Flux.fromIterable(Arrays.asList("Flux", "fromIterable"));
        flux3.subscribe(System.out::println);

        System.out.println("\nFlux.range");
        Flux<Integer> flux4 = Flux.range(1, 10);
        flux4.subscribe(System.out::println);
    }

    /**
     * 测试Mono创建流的一些接口
     */
    public static void testMono() {
        System.out.println("\nMono.just(只能一个元素)");
        Mono<String> mono1 = Mono.just("hello");
        mono1.subscribe(System.out::println);

        System.out.println("\nMono.justOrEmpty()");
        Mono<Object> mono2 = Mono.justOrEmpty(null);
        mono2.subscribe(System.out::println);

        System.out.println("\nMono.justOrEmpty(Optional)");
        Mono<Object> mono3 = Mono.justOrEmpty(Optional.empty());
        mono3.subscribe(System.out::println);
    }

    /**
     * 测试Flux通过from工厂
     */
    public static void testFluxFrom() {
        System.out.println("\nFlux.from");
        Flux<String> flux1 = Flux.from(new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {

                subscriber.onNext("hello");
                subscriber.onNext("wolrd");
                subscriber.onComplete();
            }
        });

        flux1.subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("完成了")
        );
    }

    /**
     * 测试Mono.defer()  需要订阅才能执行
     */
    public static void testMonoDefer() {
        System.out.println("\nMono.defer");
        mockRequest("Jack");

        mockRequestDefer("Rose")
                .subscribe(System.out::println);//需要订阅者触发
    }

    private static Mono<String> mockRequestDefer(String param) {
        return Mono.defer(() -> isValid(param)
                ? Mono.fromCallable(() -> getResult(param)) : Mono.error(new RuntimeException("参数不合法")));
    }

    private static Mono<String> mockRequest(String param) {
        return isValid(param) ? Mono.fromCallable(() -> getResult(param)) : Mono.error(new RuntimeException("参数不合法"));
    }

    private static boolean isValid(String param) {
        System.out.println("param is valid");
        return true;
    }

    private static String getResult(String param) {
        return "print: " + param;
    }


    /**
     * 测试Flux的订阅
     */
    public static void testFluxSubscribe() throws InterruptedException {
        System.out.println("\nFlux subscribe: doOnNext");
        Flux.range(100, 10)
                .doOnNext(System.out::println)
                .subscribe();

        System.out.println("\nFlux subscribe: doOnNext");
        Flux.range(100, 10)
                .concatWith(Flux.error(new RuntimeException("出现异常")))
                .doOnEach(System.out::println)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("处理完毕")
                );

        Thread.sleep(100);

        System.out.println("\nFlux subscribe: onError");
        Flux.from(subscriber -> {
            subscriber.onNext("hello");
            subscriber.onError(new RuntimeException("出错啦"));
            subscriber.onComplete();
        }).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("处理完毕")
        );

        Thread.sleep(100);

        System.out.println("\nFlux subscribe: onComplete");
        Flux.from(subscriber -> {
            subscriber.onNext("hello");
            subscriber.onComplete();
        }).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("处理完毕")
        );


        System.out.println("\nFlux subscribe: subscription");
        Flux.range(1, 10).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("处理完毕"),
                subscription -> {
                    subscription.request(5);//订阅5个
                    subscription.cancel();
                }
        );
    }

    /**
     * 测试Flux的映射
     */
    public static void testFluxMap() {
        System.out.println("\nFlux map: index");
        Flux.fromArray(new String[]{"Jack", "Tom", "Lily"})
                .map(x -> x+" is here")
                .index()
                .subscribe(
                        item -> {
                            System.out.println(item.getT1() + " - " + item.getT2());
                        },
                        System.err::println,
                        () -> System.out.println("处理完毕")
                );

        System.out.println("\nFlux map: timestamp");
        Flux.fromArray(new String[]{"doNext", "subscribe", "onError"})
                .map(x -> x+" is called")
                .timestamp()
                .subscribe(
                        item -> {
                            System.out.println(item.getT1() + " - " + item.getT2());
                        },
                        System.err::println,
                        () -> System.out.println("处理完毕")
                );
    }

    /**
     * 测试Flux的裁剪，过滤
     */
    public static void testFluxFilter() throws InterruptedException {
        System.out.println("\nFlux filter: any");
        Flux.just(1,2,3,4,5,6,7,8)
                .doOnNext(System.out::println)
                .any(x -> x % 2 == 0)
                .subscribe(System.out::println);

        System.out.println("\nFlux filter: skipUntilOther, takeUntilOther");
        Flux.interval(Duration.ofMillis(500))
                .map(x -> "item"+x)
                .skipUntilOther(Flux.just("start").delayElements(Duration.ofSeconds(1)))
                .takeUntilOther(Flux.just("end").delayElements(Duration.ofSeconds(3)))
                .subscribe(
                        System.out::println,
                        System.err::println,
                        () -> System.out.println("处理完毕")
                );

        Thread.sleep(5000);
    }

    /**
     * 测试Flux的收集
     */
    public static void testFluxCollect() {
        System.out.println("\nFlux collect: collectList");
        Flux.fromArray(new Integer[] {4,8,4,2,58,10})
                .collectList()
                .subscribe(System.out::println);

        System.out.println("\nFlux collect: collectSortedList");
        Flux.fromArray(new Integer[] {4,8,4,2,58,10})
                .collectSortedList()
                .subscribe(System.out::println);

        System.out.println("\nFlux collect: collectMap");
        Flux.fromArray(new Integer[] {4,8,4,2,58,10})
                .collectMap(k -> "key"+k)
                .subscribe(System.out::println);
        Flux.fromArray(new Integer[] {4,8,4,2,58,10})
                .collectMap(
                        k -> "key"+k,
                        v -> "val"+v,
                        () -> {
                            //追加map内容
                            return Stream.of(100,200,300).collect(Collectors.toMap(k->k, v->v));
                        })
                .subscribe(System.out::println);
    }

    /**
     * 测试Flux的其他
     */
    public static void testFluxOthers() {
        System.out.println("\nFlux others: repeat");
        Flux.just(1,2,3)
                .repeat(1)
                .subscribe(System.out::println);

        System.out.println("\nFlux others: defaultIfEmpty");
        Flux.empty()
                .defaultIfEmpty("stream is empty,using default")
                .subscribe(System.out::println);


        System.out.println("\nFlux others: distinct");
        Flux.just(1,1,1,1,2,2,2,2,3,3,3,1,3,4)
                .repeat(1)
                .distinct()
                .subscribe(System.out::print);


        System.out.println("\nFlux others: distinctUntilChanged");
        Flux.just(1,1,1,1,2,2,2,2,3,3,3,1,3,4)
                .distinctUntilChanged()
                .subscribe(System.out::print);

        System.out.println("\n\nFlux others: reduce");
        Flux.just(1,2,3)
                .reduce(1, (x, y) -> x + y)
                .subscribe(System.out::println);


        System.out.println("\nFlux others: scan");
        Flux.just(1,2,3)
                .scan(1, (x, y) -> {
                    System.out.println("x=" + x + " y=" + y);
                    return x + y;
                })
                .subscribe(System.out::println);

        System.out.println("\nFlux others: 基于scan的滑动窗口");
        int window = 5;
        Flux.just(1,2,3,4,5,6)
                .index()
                .scan(new int[window], (array, item) -> {
                    array[(int)(item.getT1() % window)] = item.getT2();
                    return array;
                })
                .skip(window) //窗口填满
                .map(array -> Arrays.stream(array).sum() / window) //求窗口内的平均值
                .subscribe(System.out::println);

        System.out.println("\nFlux others: thenMany");
        Flux.just(1,2,3)
                .thenMany(Flux.just(4,5,6))
                .subscribe(System.out::println);
    }

    /**
     * 测试Flux的组合
     */
    public static void testFluxStreamMerge() throws InterruptedException {
        System.out.println("\nFlux stream: concat");
        Flux.concat(Flux.just(1,2,3).delayElements(Duration.ofMillis(10)),
                        Flux.just(4,5,6).delayElements(Duration.ofMillis(10)))
                .subscribe(System.out::println);

        Thread.sleep(100);

        System.out.println("\nFlux stream: merge");
        Flux.merge(Flux.just(1,2,3).delayElements(Duration.ofMillis(10)),
                        Flux.just(4,5,6).delayElements(Duration.ofMillis(10)))
                .subscribe(System.out::println);

        Thread.sleep(100);

        System.out.println("\nFlux stream: zip，控制流以最小的为准");
        Flux.zip(Flux.range(1,10).delayElements(Duration.ofMillis(10)),
                        Flux.range(1,8).delayElements(Duration.ofMillis(10)))
                .subscribe(System.out::println);

        Thread.sleep(1000);

        System.out.println("\nFlux stream: combineLatest");
        Flux.combineLatest(
                        Flux.range(1, 10).delayElements(Duration.ofMillis(10)),
                        Flux.range(10, 10).delayElements(Duration.ofMillis(20)),
                        (x, y) -> x + "--" + y
                )
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    /**
     * 测试Flux的批处理
     */
    public static void testFluxStreamBatch() {
        System.out.println("\nFlux stream: windowUntil");
        Flux.range(101, 10)
                .windowUntil(ReactorTester::isThree, false).subscribe(
                        window -> window.collectList().subscribe(System.out::println)
                );

        System.out.println("\nFlux stream: groupBy");
        Flux.range(101, 10)
                .groupBy(key -> (key % 2 == 0) ? "偶数" : "奇数")
                .subscribe(unicastGroupedFlux -> {
                    unicastGroupedFlux.collectList().subscribe(System.out::println);
                });
    }

    /**
     * 三的倍数
     * @return
     */
    private static boolean isThree(int num) {
        return num % 3 == 0;
    }

    /**
     * 测试Flux的FlatMap、 concatMap 和 flatMap
     * @throws InterruptedException
     */
    public static void testFluxStreamFlatMap() throws InterruptedException {
        System.out.println("\nFlux stream: flatMap");
        Flux.just(Arrays.asList(1,2,3), Arrays.asList("x","y","z"), Arrays.asList("张", "赵", "王"))
                .doOnNext(System.out::println)
                .flatMap(item -> Flux.fromIterable(item).doOnSubscribe(sub -> System.out.println("订阅了")).delayElements(Duration.ofMillis(10)))
                .subscribe(System.out::println);

        Thread.sleep(1000);

        System.out.println("\nFlux stream: concatMap");
        Flux.just(Arrays.asList(1,2,3), Arrays.asList("x","y","z"), Arrays.asList("张", "赵", "王"))
                .doOnNext(System.out::println)
                .concatMap(item -> Flux.fromIterable(item).doOnSubscribe(sub -> System.out.println("订阅了")).delayElements(Duration.ofMillis(10)))
                .subscribe(System.out::println);

        Thread.sleep(1000);

        System.out.println("\nFlux stream: flatMapSequential");
        Flux.just(Arrays.asList(1,2,3), Arrays.asList("x","y","z"), Arrays.asList("张", "赵", "王"))
                .doOnNext(System.out::println)
                .flatMapSequential(item -> Flux.fromIterable(item).doOnSubscribe(sub -> System.out.println("订阅了")).delayElements(Duration.ofMillis(10)))
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    /**
     * 测试Flux的元素采样
     */
    public static void testFluxStreamSample() throws InterruptedException {
        System.out.println("\nFlux stream: 固定采样，sample");
        Flux.range(1,100).delayElements(Duration.ofMillis(10))
                .sample(Duration.ofMillis(200))
                .subscribe(System.out::println);

        Thread.sleep(1000);

        System.out.println("\nFlux stream: 随机采样，sampleTimeout");
        Flux.range(1,100).delayElements(Duration.ofMillis(10))
                .sampleTimeout(item -> Mono.delay(Duration.ofMillis(new Random().nextInt(100))))
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    /**
     * 测试Flux的流转化为阻塞结构
     */
    public static void testFluxStreamBlock() {
        System.out.println("\nFlux stream to block: toIterable");
        Flux.just(1,2,3,4,5).toIterable().forEach(System.out::println);

        System.out.println("\nFlux stream to block: toStream");
        Flux.just(1,2,3,4,5).toStream().forEach(System.out::println);

        System.out.println("\nFlux stream to block: blockFirst");
        System.out.println(Flux.just(1,2,3,4,5).blockFirst());

        System.out.println("\nFlux stream to block: blockLast");
        System.out.println(Flux.just(1,2,3,4,5).blockLast());
    }

    /**
     * 测试Flux的流物化materialize
     */
    public static void testFluxStreamMaterialize() throws InterruptedException {
        System.out.println("\nFlux stream: materialize");
        Flux.just(1,2,3)
                .delayElements(Duration.ofMillis(500))
                //.publishOn(Schedulers.parallel())
                .doOnEach(x -> {
                    System.out.println(Thread.currentThread().getName() + "-->" + x);
                })
                .concatWith(Flux.error(new RuntimeException("出错啦")))
                .materialize()//物化异常为普通信号
                .subscribe(y -> {
                    System.out.println(Thread.currentThread().getName() + "===" + y);
                });

        Thread.sleep(2000);

        System.out.println("\nFlux stream: materialize");
        Flux.just(1,2,3)
                .delayElements(Duration.ofMillis(500))
                .concatWith(Flux.error(new RuntimeException("出错啦")))
                .materialize()
                .doOnEach(x -> {
                    System.out.println(Thread.currentThread().getName() + "-->" + x);
                })
                .dematerialize()//抛出错误，不物化
                .subscribe(y -> {
                    System.out.println(Thread.currentThread().getName() + "===" + y);
                });

        Thread.sleep(2000);
    }
}
