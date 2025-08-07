## Java 8-lambda特性

> 注意：lambda表达式可以使代码看起来简洁，但一定程度上增加了代码的可读性以及调试的复杂性，所以在使用时应尽量是团队都熟悉使用，要么干脆就别用，不然维护起来是件较痛苦的事。

### 1. lambda表达式的语法

#### 1.1 基本语法: 
    
    (parameters) -> expression  或  (parameters) ->{ statements; }

#### 1.2 前提条件:
必须有一个函数式接口， 有且只有一个
可以用@FunctionalInterface对接口进行修饰

#### 1.3 lambda表达式特征

- 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。

- 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。

- 可选的大括号：如果主体包含了一个语句，就不需要使用大括号。

- 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定明表达式返回了一个数值。
---
    //1. 不需要参数,返回值为 1 
    () -> 1  
      
    //2. 接收一个参数(数字类型),返回其2倍的值  
    x -> 2 * x  
      
    //3. 接受2个参数(数字),并返回他们的差值  
    (x, y) -> x – y  
      
    //4. 接收2个int型整数,返回他们的和  
    (int x, int y) -> x + y  
      
    //5. 接受一个 String 对象,并在控制台打印,不返回任何值(看起来像是返回void)  
    (String str) -> System.out.print(str)

    For example:
        //old
        new Thread((new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类 实现线程");
            }
        })).start();
        //lambda
        new Thread( () -> System.out.println("java8 lambda实现线程")).start();
---        

#### 1.4 方法引用

> 为什么要引用？
> 方法逻辑已经有现成的方法实现了，避免重复逻辑
>
> 方法被引用的条件？``
> 入参完成相同，出参可以兼容
    
1.4.1 类::静态方法
> ClassName::staticMethod
> (x) -> String.valueOf(x) 简化为：x -> String::valueOf

1.4.2 对象::实例方法
> objectName::instanceMethod
> (x) -> System.out::println()
> (x, y) -> x.equals(y)  简化为： (x, y) -> objectName::equals

1.4.3 构造函数
> ClassName :: new
> 无参的构造方法， () -> new User()， 简化为 User::new
> 有参的构造方法, id -> new User(id)， 简化为 User::new (编译器自动会推导，寻找对应匹配的构造方法)

1.4.4 子类引用父类
> super :: 方法，如：super::toString

1.4.5 子类引用自己的方法
> this::方法，如：this::toString

1.4.6 new数组
> type[]::new
> int[]::new

### 2. 常见的函数式接口及用法

#### 2.1 Runnable/Callable
参见FunctionalInterfaceLambdaTester

#### 2.2 Supplier/Consumer
参见FunctionalInterfaceLambdaTester

#### 2.3 Comparator
参见FunctionalInterfaceLambdaTester

#### 2.4 Predicate
参见FunctionalInterfaceLambdaTester

#### 2.4 Function
参见FunctionalInterfaceLambdaTester

### 3. lambda实现原理
> lambda表达式最终就是一个匿名内部类实现
>
> 在编译的时候，“脱糖”， 即转为LambdaMetafactory构建【对应接口】的匿名内部类的调用，
> 
> 在执行的时候，通过ASM技术生成新的匿名内部类，并动态修改原Class文件，将调用新构建的内部类代码编入到原始类中

```java
    LambdaMetafactory.java

    public static CallSite metafactory(MethodHandles.Lookup caller,
                                       String invokedName,
                                       MethodType invokedType,
                                       MethodType samMethodType,
                                       MethodHandle implMethod,
                                       MethodType instantiatedMethodType){
    
}
```

原始代码：
```java
public static void main(String[] args) {
    List<String> strList = Arrays.asList(.....);
    strList.forEach(s -> {
        System.out.println(s);
    });
}
```

编译后
```java
public static void main(String[] args) {
 List<String> strList = Arrays.asList("......");
 strList.forEach(
        (Consumer<String>)LambdaMetafactory.metafactory(
            null, null, null,
            (Ljava/lang/Object;)V,
                lambda$main$0(java.lang.String ),
                (Ljava/lang/String;)V)()
        );
}

private static void lambda$main$0(String s) {
    System.out.println(s);
}

```

执行时，会生成临时的匿名内部类，并编入调用代码