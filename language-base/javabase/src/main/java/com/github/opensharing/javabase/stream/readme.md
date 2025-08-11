## Java 8-Stream API

### 1.简介
Java 8 API添加了一个新的抽象称为流Stream，可以让你以一种声明的方式处理数据。

Stream 使用一种类似用 SQL 语句从数据库查询数据的直观方式来提供一种对 Java 集合运算和表达的高阶抽象。

Stream API可以极大提高Java程序员的生产力，让程序员写出高效率、干净、简洁的代码。

这种风格将要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等。

元素流在管道中经过中间操作（intermediate operation）的处理，最后由最终操作(terminal operation)得到前面处理的结果。

```
+--------------------+       +------+   +------+   +---+   +-------+
| stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
+--------------------+       +------+   +------+   +---+   +-------+
```

1.1 特点：
1. 不是数据结构，不会保存数据，专注对容器对象的聚合操作。
2. 不会修改原来的数据源，它会将操作后的数据保存到另外一个对象中。（保留意见：毕竟peek方法可以修改流中元素）
3. 惰性求值（延迟处理），流在中间处理过程中，只是对操作进行了记录，并不会立即执行，需要等到执行终止操作的时候才会进行实际的计算。
4. 提供串行/并行两种方式，使用ForkJoin框架（提升多核CPU的优势，分而治之）拆分任务。
5. 主要是提高编程效率。

1.2 分类：         
> 详细看图：docs/image/stream-methods.png
>
> 无状态：指元素的处理不受之前元素的影响；
> 
>有状态：指该操作只有拿到所有元素之后才能继续下去。
> 
> 非短路操作：指必须处理所有元素才能得到最终结果；
> 
> 短路操作：指遇到某些符合条件的元素就可以得到最终结果，如 A || B，只要A为true，则无需判断B的结果。 

![](https://img-blog.csdnimg.cn/20181223012834784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3lfa195,size_16,color_FFFFFF,t_70)


### 2. Stream使用

#### 2.1 创建操作
* Collection下的 stream() 和 parallelStream(): 构建集合串行流，并行流
* Stream.of()、iterate()、generate()： 静态方法构建流
* Arrays.stream()：将数组转成流
* BufferedReader.lines()：将每行内容转成流
* Pattern.splitAsStream()：将字符串分隔成流
* IntStream/LongStream.range/RangeClose: 创建数字流
* Random.ints/longs/doubles: 随机数字流

#### 2.2 中间操作（一般可以有0个或多个）
> 筛选与切片
* filter：过滤流中的某些元素
* limit(n)：获取n个元素
* skip(n)：跳过n元素，配合limit(n)可实现分页
* distinct：通过流中元素的 hashCode() 和 equals() 去除重复元素

> 映射
* map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
* flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。
* flatMapToInt：把流中每个元素先映射成一个 IntStream，再把所有 IntStream 扁平合并成一条 IntStream，常用于“元素 → 多个 int”的场景
* flatMapToLong：同理。
* flatMapToDouble：同理。
* mapToInt：把 流中的每个元素映射成int并返回一个IntStream，专门用于后续做数值计算（如 sum、max、average 等），避免装箱/拆箱开销
* mapToLong：同理。
* mapToDouble：同理。

> 排序
* sorted()：自然排序，流中元素需实现Comparable接口
* sorted(Comparator com)：定制排序，自定义Comparator排序器

> 消费
* peek：如同于map，能得到流中的每一个元素。但map接收的是一个Function表达式，有返回值；而peek接收的是Consumer表达式，没有返回值。

#### 2.3 终止操作（有且只有1个）

> 匹配、聚合操作
* allMatch：接收一个 Predicate 函数，当流中每个元素都符合该断言时才返回true，否则返回false
* noneMatch：接收一个 Predicate 函数，当流中每个元素都不符合该断言时才返回true，否则返回false
* anyMatch：接收一个 Predicate 函数，只要流中有一个元素满足该断言则返回true，否则返回false
* findFirst：返回流中第一个元素
* findAny：返回流中的任意元素
* count：返回流中元素的总个数
* max：返回流中元素最大值
* min：返回流中元素最小值

> 规约操作
* reduce：第一次执行时，第一个参数为流中的第一个元素，第二个参数为流中元素的第二个元素；第二次执行时，第一个参数为第一次函数执行的结果，第二个参数为流中的第三个元素；依次类推

> 收集操作
* collect：接收一个Collector实例，将流中元素收集成另外一个数据结构。通过Collectors去构建Collector实例。

> 遍历
* forEach：自然遍历，在并行流下会乱序输出。
* forEachOrdered：保障按元素在源中的原始顺序依次消费，即使流是并行的。

#### 2.4 其他特殊操作
> 如果流式链多次使用串并行操作， 以最后一个为准。 比如.sequential().parallel() 则以并行处理。
* sequential：串行流
* parallel：并行流程
* unordered()：把 Stream 标记为无序，允许后续操作跳过顺序保证、提升并行效率
* concat(Stream a, Stream b)：把两个流顺序拼接成一条新流，先 a 后 b
* toArray：把流里的元素收集成数组；无参返回 Object[]，带 IntFunction 可返回指定类型的数组

### 3. 注意避坑
> 谨记：Stream 是一次性、无副作用、惰性求值的数据管道，把它当成纯函数式流水线即可避开 90% 的坑。

| 踩坑点                          | 反面示例 & 后果                                                                                 | 正确写法                                                                                                  | 规避要点                               |
| -------------------------- | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- | ---------------------------------- |
| **修改数据源**     | `list.stream().peek(list::remove).collect(toList());` → `ConcurrentModificationException` | `list.stream().collect(Collectors.toList()).removeAll(...);`                                          | **绝不改动原集合**，先 `collect` 再操作副本      |
| **副作用**       | `peek(System.out::println).count();` 并行时打印顺序乱/不可测试                                        | `.forEach(System.out::println);`                                                                      | **保持 无状态lambda 纯函数**，避免 `peek` 做写文件、发消息   |
| **忘终端操作**     | `list.stream().filter(x -> x > 3);` 管道未执行，结果为空                                            | `list = list.stream().filter(x -> x > 3).collect(Collectors.toList());`                               | **必须加终端操作**（`collect`, `forEach`…） |
| **并行流阻塞公共池**  | `Files.lines(path).parallel().map(this::httpCall).count();` → 卡住整个 JVM                    | `ForkJoinPool pool = new ForkJoinPool(8); pool.submit(() -> Files.lines(path).parallel()...).join();` | **IO/阻塞任务不用默认并行流**                 |
| **装箱拆箱**      | `list.stream().map(i -> i * 2).sum();` 大量 Integer 对象，GC 压力                                | `list.stream().mapToInt(i -> i * 2).sum();`                                                           | **用基本类型专用流**（IntStream、LongStream） |
| **排序/去重 OOM** | `list.stream().sorted().collect(toList());` 大数据量内存溢出                                      | `list.stream().limit(1000).sorted().collect(toList());`                                               | **先过滤/分片再排序**                      |
| **无限流死循环**    | `Stream.generate(Math::random).collect(toList());` 永不结束                                   | `Stream.generate(Math::random).limit(100).collect(toList());`                                         | **无限流必须加 `limit`/`findFirst`**     |

#### 并行流阻塞公共池的原因
> 并行流阻塞公共池的根源是 所有并行流默认共享同一个 ForkJoinPool.commonPool()，一旦任务阻塞（IO、锁、sleep），就会耗尽该池的全部工作线程，导致整个 JVM 内任何后续并行任务都无法调度，形成全局假死。

#### 避免阻塞公共池的 checklist
> 记住：阻塞任务 + 默认并行流 = JVM 全局阻塞。

| 场景            | 建议                                           |
| ------------- | -------------------------------------------- |
| IO、网络、锁、sleep | **别用默认并行流**                                  |
| CPU 计算密集      | 默认并行流 OK                                     |
| 需要可控并发度       | 自定义 `ForkJoinPool`                           |
| 已有线程池         | 用 `CompletableFuture.runAsync(task, myPool)` |


### 参考博客：
> 1. https://blog.csdn.net/y_k_y/article/details/84633001
> 2. https://www.runoob.com/java/java8-streams.html