### Java 8-Stream API

#### 1.简介
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
> 1. 不是数据结构，不会保存数据。
> 2. 不会修改原来的数据源，它会将操作后的数据保存到另外一个对象中。（保留意见：毕竟peek方法可以修改流中元素）
> 3. 惰性求值，流在中间处理过程中，只是对操作进行了记录，并不会立即执行，需要等到执行终止操作的时候才会进行实际的计算。

1.2 分类：         
> 详细看图：docs/image/stream-methods.png
>
> 无状态：指元素的处理不受之前元素的影响；
> 有状态：指该操作只有拿到所有元素之后才能继续下去。
> 非短路操作：指必须处理所有元素才能得到最终结果；
> 短路操作：指遇到某些符合条件的元素就可以得到最终结果，如 A || B，只要A为true，则无需判断B的结果。

![](https://img-blog.csdnimg.cn/20181223012834784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3lfa195,size_16,color_FFFFFF,t_70)


#### 参考博客：
> 1. https://blog.csdn.net/y_k_y/article/details/84633001
> 2. https://www.runoob.com/java/java8-streams.html