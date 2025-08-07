## 深入理解类加载全过程（JAVA从编写--》编译--》执行）

#### 1. 理论知识

- 1.1 JAVA从编译到执行: language-base/javabase/docs/image/JAVA从编译到执行.png

- 1.2 对象的内存布局: language-base/docs/image/JavaObjectLayout.png
  >[参考](https://www.cnblogs.com/JonaLin/p/13864578.html) 使用openjdk的工具jol

- 1.3 类加载过程: language-base/javabase/docs/image/类加载的过程.png（TODO：图解）

  - 1.3.1  Loading
    >1. 双亲委派，主要出于安全来考虑
    >2. LazyLoading
    >3. ClassLoader（源码解析：findInCache -> parent.loadClass -> findClass()）
    >4. 自定义类加载器

  - 1.3.2. Linking 
    >1. Verification：验证文件是否符合JVM规定
    >2. Preparation：静态成员变量赋默认值
    >3. Resolution：1）将类、方法、属性等符号引用解析为直接引用；2）常量池中的各种符号引用解析为指针、偏移量等内存地址的直接引用

  - 1.3.3. Initializing
    >1. 调用类初始化代码 <clinit>，给静态成员变量赋初始值
      
#### 2. 案例实践

- 2.1 对象的内存布局: JavaObjectLayoutObjectSizeDemo