## Java 8-lambda表达式和使用方法

> 注意：lambda表达式可以使代码看起来简洁，但一定程度上增加了代码的可读性以及调试的复杂性，所以在使用时应尽量是团队都熟悉使用，要么干脆就别用，不然维护起来是件较痛苦的事。

#### 1. lambda表达式的语法

##### 1.1 基本语法: 
    
    (parameters) -> expression  或  (parameters) ->{ statements; }
    
##### 1.2 lambda表达式特征

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

##### 1.3 方法引用

1.3.1 对象::实例方法 
> objectName::instanceMethod
> (x) -> System.out::println()
    
1.3.2 类::静态方法
> ClassName::staticMethod
> (x) -> String.valueOf(x)

1.3.3 类::实例方法
> ClassName::instanceMethod
> (x, y) -> x.equals(y)

1.3.4 构造函数
> 无参的构造方法就是类::实例方法模型，如：User::new
> 有参的构造方法, id -> new User(id)