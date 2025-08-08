package com.github.opensharing;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuples;

/**
 * ReactorTester2
 *
 * @author jwen
 * Date 2025/8/8
 */
public class ReactorTester2 {


    public static void main(String[] args) throws InterruptedException {
        //testPush();
        //testCreate();
        testGenerate();
    }

    /**
     * push
     */
    public static void testPush() {
        System.out.println("\nFlux.push");
        Flux<Integer> push = Flux.push(fluxSink -> {

            //假设这是数据库取出的数据
            Stream.of(1, 2, 3, 4, 5).forEach(data -> {
                fluxSink.next(data);
            });
        });

        push.subscribe(System.out::println);
    }

    /**
     * create
     */
    public static void testCreate() throws InterruptedException {
        System.out.println("\nFlux.create");
        Flux<String> create = Flux.create(fluxSink -> {

            new EventProcessor().bind(new EventListener<String>() {
                @Override
                public void onDataChunk(List<String> list) {
                    list.stream().forEach(fluxSink::next);
                }

                @Override
                public void onComplete() {
                    fluxSink.complete();
                }
            }).process();
        });

        create.subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("处理完成"));

        Thread.sleep(3000);
    }

    static class EventProcessor {

        private EventListener eventListener;

        EventProcessor bind(EventListener eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        void process() {
            while (new Random().nextInt(10) % 3 != 0) {
                List<String> chunkData = Arrays.asList("1", "2", "3", "4", "5", "6");
                this.eventListener.onDataChunk(chunkData);
            }
            this.eventListener.onComplete();
        }
    }

    interface EventListener<T> {

        void onDataChunk(List<T> list);

        void onComplete();
    }

    /**
     * generate
     */
    public static void testGenerate() throws InterruptedException {
        System.out.println("\nFlux.generate");
        Flux.generate(() -> {
                    List<Long> nums = new ArrayList<>();
                    nums.add(0L);
                    nums.add(1L);
                    return nums;
                }, new BiFunction<List<Long>, SynchronousSink<Long>, List<Long>>() {

                    @Override
                    public List<Long> apply(List<Long> nums, SynchronousSink<Long> sink) {

                        Long last = nums.get(nums.size() - 1);
                        Long pre = nums.get(nums.size() - 2);
                        nums.add((pre + last));
                        sink.next(last);
                        return nums;
                    }
                })
                .delayElements(Duration.ofMillis(100))
                .take(10)
                .subscribe(System.out::println);

        Thread.sleep(2000);


        System.out.println("\nFlux.generate with Tuples ");
        Flux.generate(() -> Tuples.of(0L, 1L), (state, slink) -> {
                    System.out.println("t1=" + state.getT1() + " t2=" + state.getT2());
                    slink.next(state.getT2());
                    return Tuples.of(state.getT2(), (state.getT1() + state.getT2()));
                })
                .delayElements(Duration.ofMillis(100))
                .take(10)
                .subscribe(System.out::println);

        Thread.sleep(2000);
    }
}
